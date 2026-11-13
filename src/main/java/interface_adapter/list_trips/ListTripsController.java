package interface_adapter.list_trips;

import use_case.list_trips.ListTripsInputBoundary;

public class ListTripsController {
    private final ListTripsInputBoundary interactor;

    public ListTripsController(ListTripsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}