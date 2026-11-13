package use_case.list_trips;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entity.LocationInfo;
import entity.Photo;
import entity.Trip;

public class ListTripsInteractor implements ListTripsInputBoundary {
    private static final Comparator<TripSummary> NEWEST_FIRST = Comparator.comparing(
            TripSummary::getStartTime, Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder()));
    
    private final ListTripsDataAccessInterface dataAcess;
    private final ListTripsOutputBoundary presenter;

    public ListTripsInteractor(ListTripsDataAccessInterface dataAccess, ListTripsOutputBoundary presenter) {
        this.dataAcess = dataAccess;
        this.presenter = presenter;
    }

    @Override 
    public void execute() {
        List<TripSummary> summaries = new ArrayList<>();
        try {
            for (Trip trip : dataAcess.getAllTrips()) {
                summaries.add(summarize(trip));
            }
        } catch (RuntimeException e) {
            presenter.prepareFailView("Couldn't load trips: " + e.getMessage());
            return;
        }
        summaries.sort(NEWEST_FIRST);
        presenter.prepareSuccessView(new ListTripsOutputData(summaries));
    }

    private TripSummary summarize(Trip trip) {
        List<Photo> photos = new ArrayList<>(dataAcess.getTripPhotos(trip.getId()));
        Collections.sort(photos); //chronological. photos without a time last.

        String coverPath = photos.isEmpty() ? null : dataAcess.getPhotoPath(photos.get(0).getId());

        return new TripSummary(trip.getId(), trip.getName(), trip.getStartTime(), trip.getEndTime(), photos.size(), mostCommonLocation(photos), coverPath);
    }

    /** The most frequent "City, Country" among the photos. Earliest photo wins for ties. */
    private static String mostCommonLocation(List<Photo> photos) {
        Map<String, Long> counts = photos.stream()
                .map(ListTripsInteractor::labelOf)
                .filter(label -> label != null)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static String labelOf(Photo photo) {
        if (photo.getLocation() == null || photo.getLocation().getInfo() == null) {
            return null;
        }
        LocationInfo info = photo.getLocation().getInfo();
        String label = Stream.of(info.getCity(), info.getCountry())
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(", "));
        return label.isEmpty() ? null : label;
    }
}