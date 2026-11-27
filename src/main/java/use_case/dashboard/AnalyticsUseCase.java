package use_case.dashboard;

import entity.LocationInfo;
import entity.LocationInfoType;
import entity.Trip;
import use_case.PhotoAccessException;
import use_case.PhotoAccessInterface;
import use_case.TripAccessException;
import use_case.TripAccessInterface;

import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Delegate to compute analytics for the specified type, and prepare output data.
 */
public class AnalyticsUseCase implements DashboardInputBoundary {
    private static final int RANKING_LIMIT = 30;
    private final TripAccessInterface tripAccess;
    private final PhotoAccessInterface photoAccess;
    private final DashboardOutputBoundary presenter;

    public AnalyticsUseCase(TripAccessInterface tripAccess,
                            PhotoAccessInterface photoAccess,
                            DashboardOutputBoundary dashboardOutputBoundary) {
        this.presenter = dashboardOutputBoundary;
        this.photoAccess = photoAccess;
        this.tripAccess = tripAccess;
    }
    @Override
    public void execute(DashboardInputData dashboardInputData) {
        AnalyticsType analyticsType = dashboardInputData.getAnalyticsType();
        try {
            switch (analyticsType) {
                case MOST_VISITED -> mostVisited(dashboardInputData);
                case PHOTO_ACTIVITY -> photoActivity(dashboardInputData);
            }
        } catch (PhotoAccessException | TripAccessException e) {
            presenter.prepareFailureView("Access to Photos or Trips Failed:\n" + e.getMessage());
        } catch (RuntimeException e) {
            presenter.prepareFailureView("Unexpected Failure:\n" + e.getMessage());
        }


    }

    private void mostVisited(DashboardInputData dashboardInputData) {
        Optional<LocationInfoType> locationInfoType = ((DashboardInputDataCatalog.MostVisitedInputData)dashboardInputData).locationInfoType();
        DashboardOutputData data = new DashboardOutputData(AnalyticsType.MOST_VISITED);
        LocationInfoType locationType = locationInfoType.orElse(LocationInfoType.COUNTRY); // default location type
        DashboardOutputDataCatalog.MostVisited mostVisited = computeMostVisited(locationType);
        if (mostVisited.placeVisits().isEmpty()) {
           presenter.prepareFailureView("You haven't imported any trips :)");
           return;
        }
        data.addDashboardItem(mostVisited);
        presenter.prepareSuccessView(data);
    }

    private void photoActivity(DashboardInputData dashboardInputData) {
        AnalyticsOptions.TimeGrouping grouping = ((DashboardInputDataCatalog.PhotoActivityInputData) dashboardInputData)
                .grouping()
                .orElse(AnalyticsOptions.TimeGrouping.MONTH);
        Map<LocalDateTime, Integer> dateToCount = photoAccess.getPhotos().stream()
                .collect(
                        Collectors.groupingBy(
                                photo -> smoothenDate(photo.getTime(), grouping),
                                Collectors.summingInt(photo -> 1))
                );
        if (dateToCount.isEmpty()) {
            presenter.prepareFailureView("You haven't taken any photos :)");
            return;
        }
        DashboardItem item = new DashboardOutputDataCatalog.PhotoActivity(grouping, dateToCount);
        DashboardOutputData data = new DashboardOutputData(AnalyticsType.PHOTO_ACTIVITY);
        data.addDashboardItem(item);
        presenter.prepareSuccessView(data);
    }

    private LocalDateTime smoothenDate(LocalDateTime localDateTime, AnalyticsOptions.TimeGrouping grouping) {
        return switch (grouping) {
            case MONTH -> YearMonth.from(localDateTime)
                    .atDay(1)
                    .atStartOfDay();
            case YEAR -> Year.from(localDateTime)
                    .atMonth(1)
                    .atDay(1)
                    .atStartOfDay();
        };
    }

    private DashboardOutputDataCatalog.MostVisited computeMostVisited(LocationInfoType locationInfoType) {
        Function<LocationInfo, String> locationSelector = info -> info.getType(locationInfoType);
        List<Trip> trips = tripAccess.getTrips();
        Map<String, Long> numVisitsByPlace = trips.stream()
                .flatMap(trip -> photoAccess.getPhotosByTripId(trip.getId()).stream()
                        .map(photo -> locationSelector.apply(photo.getLocation().getInfo()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
                        .stream()
                )
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, List<LocationInfo>> locationsByPlace = trips.stream()
                .flatMap(trip -> photoAccess.getPhotosByTripId(trip.getId()).stream())
                .map(photo -> photo.getLocation().getInfo())
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(locationSelector));

        List<DashboardOutputDataCatalog.Visit> visits = locationsByPlace
                .entrySet()
                .stream()
                .map(entry -> {
                    String place = entry.getKey();
                    List<LocationInfo> locations = entry.getValue();
                    Integer numberOfTimesVisited = numVisitsByPlace.get(place).intValue();
                    List<String> landmarks = locations.stream()
                            .map(LocationInfo::getLandmark)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();
                    List<LocalDate> dates = trips.stream()
                            .filter(trip -> getPlacesByTrip(trip, locationSelector).contains(place))
                            .map(Trip::getStartTime)
                            .filter(Objects::nonNull)
                            .distinct()
                            .map(LocalDateTime::toLocalDate)
                            .toList();
                    return new DashboardOutputDataCatalog.Visit(place, numberOfTimesVisited, dates, landmarks);
                })
                .sorted(Comparator.comparingInt(DashboardOutputDataCatalog.Visit::numberOfTimesVisited).reversed())
                .limit(RANKING_LIMIT)
                .toList();

        return new DashboardOutputDataCatalog.MostVisited(locationInfoType, visits);
    }

    private List<String> getPlacesByTrip(Trip trip, Function<LocationInfo, String> locationSelector) {
        return photoAccess.getPhotosByTripId(trip.getId()).stream()
                .map(photo -> locationSelector.apply(photo.getLocation().getInfo()))
                .toList();
    }
}
