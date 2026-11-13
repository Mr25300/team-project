package interface_adapter.list_trips;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import use_case.list_trips.ListTripsOutputData;
import use_case.list_trips.TripSummary;

import static org.junit.jupiter.api.Assertions.*;

class ListTripsPresenterTest {

    private static LocalDateTime at(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 12, 0);
    }

    @Test
    void dateRangeFormats() {
        assertEquals("16 May 2025", ListTripsPresenter.formatDateRange(at(2025, 5, 16), at(2025, 5, 16)));
        assertEquals("16–17 May 2025", ListTripsPresenter.formatDateRange(at(2025, 5, 16), at(2025, 5, 17)));
        assertEquals("30 May – 2 Jun 2025", ListTripsPresenter.formatDateRange(at(2025, 5, 30), at(2025, 6, 2)));
        assertEquals("30 Dec 2024 – 2 Jan 2025",
                ListTripsPresenter.formatDateRange(at(2024, 12, 30), at(2025, 1, 2)));
        assertEquals("Unknown dates", ListTripsPresenter.formatDateRange(null, null));
    }

    @Test
    void detailsFormats() {
        assertEquals("Montreal, Canada · 5 photos", ListTripsPresenter.formatDetails("Montreal, Canada", 5));
        assertEquals("Unknown location · 1 photo", ListTripsPresenter.formatDetails(null, 1));
    }

    @Test
    void successFillsStateClearsErrorAndFires() {
        TripListViewModel viewModel = new TripListViewModel();
        viewModel.getState().setErrorMessage("old error");
        int[] fires = {0};
        viewModel.addPropertyChangeListener(evt -> fires[0]++);

        TripSummary summary = new TripSummary("t1", "Montreal Weekend", at(2025, 5, 16), at(2025, 5, 17),
                5, "Montreal, Canada", "/c.jpg");
        new ListTripsPresenter(viewModel).prepareSuccessView(new ListTripsOutputData(List.of(summary)));

        TripListItem item = viewModel.getState().getItems().get(0);
        assertEquals("t1", item.getTripId());
        assertEquals("Montreal Weekend", item.getName());
        assertEquals("16–17 May 2025", item.getDateRange());
        assertEquals("Montreal, Canada · 5 photos", item.getDetails());
        assertEquals("/c.jpg", item.getCoverPhotoPath());
        assertNull(viewModel.getState().getErrorMessage());
        assertEquals(1, fires[0]);
    }

    @Test
    void failSetsErrorAndFires() {
        TripListViewModel viewModel = new TripListViewModel();
        int[] fires = {0};
        viewModel.addPropertyChangeListener(evt -> fires[0]++);

        new ListTripsPresenter(viewModel).prepareFailView("boom");

        assertEquals("boom", viewModel.getState().getErrorMessage());
        assertEquals(1, fires[0]);
    }
}