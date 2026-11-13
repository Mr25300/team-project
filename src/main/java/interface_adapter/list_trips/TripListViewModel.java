package interface_adapter.list_trips;

import interface_adapter.ViewModel;
import interface_adapter.ViewNames;

public class TripListViewModel extends ViewModel<TripListState> {
    public TripListViewModel() {
        super(ViewNames.TRIPS);
        setState(new TripListState());
    }
}