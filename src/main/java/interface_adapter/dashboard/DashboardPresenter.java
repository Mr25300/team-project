package interface_adapter.dashboard;

import entity.LocationInfoType;
import use_case.dashboard.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DashboardPresenter implements DashboardOutputBoundary {
    private final DashboardViewModel dashboardViewModel;

    public DashboardPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }

    public void prepareSuccessView(DashboardOutputData dashboardOutputData) {
        AnalyticsType analyticsType = dashboardOutputData.getAnalyticsType();
        switch (analyticsType) {
            case MOST_VISITED -> mostVisited(dashboardOutputData);
            case PHOTO_ACTIVITY -> photoActivity(dashboardOutputData);
        }
    }
    public void prepareFailureView(String msg) {
        DashboardState state = new DashboardState();
        state.addItemState(new DashboardStateCatalog.MessageState(
                msg, 22f
        ));
        dashboardViewModel.setState(state);
        dashboardViewModel.firePropertyChanged();
    }

    private void mostVisited(DashboardOutputData data) {
        // for this analytics, there's only a table
        DashboardOutputDataCatalog.MostVisited mostVisited =
                (DashboardOutputDataCatalog.MostVisited) data.getDashboardItems().getFirst();
        List<List<String>> renderedEntries = mostVisited.placeVisits().stream()
                .map(placeVisit -> List.<String>of(placeVisit.place(),
                                placeVisit.numberOfTimesVisited().toString(),
                                String.join(", ", placeVisit.dates().stream().map(LocalDate::toString).toList()),
                                String.join(", ", placeVisit.landmarks()))
                )
                .toList();
        String typeOfPlace = resolveLocationTypeString(mostVisited.typeOfPlace());
        DashboardState currentState = buildMostVisitedState(typeOfPlace, renderedEntries);
        dashboardViewModel.setState(currentState);
        dashboardViewModel.firePropertyChanged();
    }

    private void photoActivity(DashboardOutputData dashboardOutputData) {
        DashboardOutputDataCatalog.PhotoActivity data = (DashboardOutputDataCatalog.PhotoActivity) dashboardOutputData.getDashboardItems().getFirst();
        Map<LocalDateTime, Integer> dateToCount = data.dateToCount();
        AnalyticsOptions.TimeGrouping grouping = data.grouping();
        LocalDateTime earliest = dateToCount.keySet().stream()
                .sorted().toList().getFirst();
        List<Integer> x_vals = new ArrayList<>();
        List<Integer> y_vals = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        dateToCount.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    LocalDateTime date = entry.getKey();
                    x_vals.add(dateToIndex(date, grouping, earliest));
                    y_vals.add(entry.getValue());
                    dates.add(formatDate(date, grouping));
                    counts.add(entry.getValue());
                });

        DashboardStateCatalog.LinePlotState linePlotState = new DashboardStateCatalog.LinePlotState(x_vals, y_vals, dates, counts);
        DashboardStateCatalog.NestedState nestedState = new DashboardStateCatalog.NestedState(
                List.of(
                        new DashboardStateCatalog.DropdownState(
                                "photo-activity",
                                "Photo Activity By...",
                                List.of(
                                        "Year",
                                        "Month"
                                )
                        ),
                        new DashboardStateCatalog.HeadingState("Your photo activity (counts) by " + resolveGroupingString(grouping), 15f),
                        linePlotState
                )
        );
        DashboardState state =  new DashboardState();
        state.addItemState(nestedState);
        dashboardViewModel.setState(state);
        dashboardViewModel.firePropertyChanged();
    }

    private String formatDate(LocalDateTime date, AnalyticsOptions.TimeGrouping timeGrouping) {
        return switch (timeGrouping) {
            case YEAR -> date.format(DateTimeFormatter.ofPattern("yyyy"));
            case MONTH -> date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        };
    }

    private int dateToIndex(LocalDateTime date, AnalyticsOptions.TimeGrouping timeGrouping, LocalDateTime start) {
        return switch (timeGrouping) {
            case YEAR -> Math.toIntExact(ChronoUnit.YEARS.between(Year.from(start), Year.from(date)));
            case MONTH -> Math.toIntExact(ChronoUnit.MONTHS.between(YearMonth.from(start), YearMonth.from(date)));
        };
    }

    private DashboardState buildMostVisitedState(String typeOfPlace, List<List<String>> renderedEntries) {
        DashboardItemState state = new DashboardStateCatalog.NestedState(List.of(
            new DashboardStateCatalog.DropdownState(
                    "most-visited",
                    "Most visited by ...",
                    List.of(
                            "Country",
                            "Province/State",
                            "County",
                            "City",
                            "Continent"
                    )
            ),
            new DashboardStateCatalog.TableState(
                    "Most Visited " + typeOfPlace,
                    List.of(typeOfPlace, "Number of Times Visited", "Dates Visited", "Landmarks Visited"),
                    renderedEntries
            )
        ));
        DashboardState currentState = dashboardViewModel.getState();
        currentState.removeAllStates();
        currentState.addItemState(state);
        return currentState;
    }

    private String resolveLocationTypeString(LocationInfoType locationInfoType) {
        Map<LocationInfoType, String> stringMap = Map.of(
                LocationInfoType.COUNTRY, "Country",
                LocationInfoType.CITY, "City",
                LocationInfoType.CONTINENT, "Continent",
                LocationInfoType.STATE, "Province/State",
                LocationInfoType.COUNTY, "County"
        );
        return stringMap.get(locationInfoType);
    }

    private String resolveGroupingString(AnalyticsOptions.TimeGrouping timeGrouping) {
        Map<AnalyticsOptions.TimeGrouping, String> stringMap = Map.of(
                AnalyticsOptions.TimeGrouping.YEAR, "year",
                AnalyticsOptions.TimeGrouping.MONTH, "month"
        );
        return stringMap.get(timeGrouping);
    }
}