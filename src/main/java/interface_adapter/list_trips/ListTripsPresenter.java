package interface_adapter.list_trips;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import use_case.list_trips.ListTripsOutputBoundary;
import use_case.list_trips.ListTripsOutputData;
import use_case.list_trips.TripSummary;

public class ListTripsPresenter implements ListTripsOutputBoundary {
    public static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("d", Locale.ENGLISH);
    public static final DateTimeFormatter DAY_MONTH = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH);
    public static final DateTimeFormatter FULL = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private final TripListViewModel viewModel;

    public ListTripsPresenter(TripListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override 
    public void prepareSuccessView(ListTripsOutputData outputData) {
        List<TripListItem> items = outputData.getTrips().stream()
                .map(ListTripsPresenter::toItem)
                .collect(Collectors.toList());

        TripListState state = viewModel.getState();
        state.setItems(items);
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override 
    public void prepareFailView(String errorMessage) {
        TripListState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    private static TripListItem toItem(TripSummary trip) {
        return new TripListItem(trip.getId(), trip.getName(),
                formatDateRange(trip.getStartTime(), trip.getEndTime()),
                formatDetails(trip.getLocationLabel(), trip.getPhotoCount()),
                trip.getCoverPhotoPath());
    }

    /** "13 Nov 2026", "13-14 Nov 2026", "13 Nov - 2 Dec 2026" */
    static String formatDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            return "Unknown dates";
        }
        if (end == null || start.toLocalDate().equals(end.toLocalDate())) {
            return FULL.format(start);
        }
        if (start.getYear() != end.getYear()) {
            return FULL.format(start) + " – " + FULL.format(end);
        }
        if (start.getMonth() != end.getMonth()) {
            return DAY_MONTH.format(start) + " – " + FULL.format(end);
        }
        return DAY.format(start) + "–" + FULL.format(end);
    }

    /** "Montreal, Canada · 5 photos". */
    static String formatDetails(String locationLabel, int photoCount) {
        String location = locationLabel == null ? "Unknown location" : locationLabel;
        String photos = photoCount == 1 ? "1 photo" : photoCount + " photos";
        return location + " · " + photos;
    }
}