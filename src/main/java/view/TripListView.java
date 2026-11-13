package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import interface_adapter.list_trips.ListTripsController;
import interface_adapter.list_trips.TripListItem;
import interface_adapter.list_trips.TripListState;
import interface_adapter.list_trips.TripListViewModel;
import view.components.ThumbnailCache;
import view.components.TripCardRenderer;

/** The Trips tab: scrollable list of trip cards. */
public class TripListView extends JPanel implements ActivatableView, PropertyChangeListener {
    static final String EMPTY_MESSAGE = "No trips yet - import photos to get started.";

    private final TripListViewModel viewModel;
    private final DefaultListModel<TripListItem> listModel = new DefaultListModel<>();
    private final JList<TripListItem> tripList = new JList<>(listModel);
    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
    private ListTripsController listTripsController;

    public TripListView(TripListViewModel viewModel, ThumbnailCache thumbnails) {
        super(new BorderLayout());
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        tripList.setCellRenderer(new TripCardRenderer(thumbnails, tripList::repaint));
        tripList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        messageLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        messageLabel.setVisible(false);

        add(messageLabel, BorderLayout.NORTH);
        add(new JScrollPane(tripList), BorderLayout.CENTER);
    }

    public void setListTripsController(ListTripsController controller) {
        this.listTripsController = controller;
    }

    @Override 
    public void onActivated() {
        if (listTripsController != null) {
            listTripsController.execute();
        }
    }

    @Override 
    public void propertyChange(PropertyChangeEvent evt) {
        SwingThread.runOnEdt(() -> render(viewModel.getState()));
    }

    private void render(TripListState state) {
        listModel.clear();
        listModel.addAll(state.getItems());

        if (state.getErrorMessage() != null) {
            showMessage(state.getErrorMessage());
        } else if (state.getItems().isEmpty()) {
            showMessage(EMPTY_MESSAGE);
        } else {
            messageLabel.setVisible(false);
        }
    }

    private void showMessage(String text) {
        messageLabel.setText(text);
    }

    // Package-private accessors for tests.
    DefaultListModel<TripListItem> getListModel() { return listModel; }
    JLabel getMessageLabel() { return messageLabel; }
}