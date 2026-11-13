package view;

import java.util.List;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import interface_adapter.list_trips.ListTripsController;
import interface_adapter.list_trips.TripListItem;
import interface_adapter.list_trips.TripListViewModel;
import view.components.ThumbnailCache;

import static org.junit.jupiter.api.Assertions.*;

class TripListViewTest {
    private TripListViewModel viewModel;
    private TripListView view;

    @BeforeEach
    void setUp() {
        viewModel = new TripListViewModel();
        view = new TripListView(viewModel, new ThumbnailCache(64, 10));
    }

    private void publish(List<TripListItem> items, String error) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            viewModel.getState().setItems(items);
            viewModel.getState().setErrorMessage(error);
            viewModel.firePropertyChanged();
        });
    }

    @Test
    void rendersItems() throws Exception {
        publish(List.of(new TripListItem("t", "Trip", "1 May 2025", "X · 1 photo", null)), null);

        assertEquals(1, view.getListModel().size());
        assertFalse(view.getMessageLabel().isVisible());
    }

    @Test
    void showsEmptyMessage() throws Exception {
        publish(List.of(), null);

        assertTrue(view.getMessageLabel().isVisible());
        assertEquals(TripListView.EMPTY_MESSAGE, view.getMessageLabel().getText());
    }

    @Test
    void showsErrorMessage() throws Exception {
        publish(List.of(), "Couldn't load trips: boom");

        assertTrue(view.getMessageLabel().isVisible());
        assertEquals("Couldn't load trips: boom", view.getMessageLabel().getText());
    }

    @Test
    void activationRunsController() {
        int[] calls = {0};
        view.setListTripsController(new ListTripsController(() -> calls[0]++));

        view.onActivated();

        assertEquals(1, calls[0]);
    }
}