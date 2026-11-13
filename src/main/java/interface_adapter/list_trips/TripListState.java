package use_case.list_trips;

import java.util.ArrayList;
import java.util.List;

/** Everything the trip list screen displays */
public class TripListState {
    private List<TripListItem> items = new ArrayList<>();
    private String errorMessage;

    public List<TripListItem> getItems() { return items; }
    public void setItems(List<TripListItem> items) { this.items = List.copyOf(items); }

    /** Null when last load succeeded. */
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}