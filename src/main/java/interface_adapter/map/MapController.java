package interface_adapter.map;

import use_case.map.MapInputBoundary;
import use_case.map.input_data.SelectedTripInputData;

public class MapController {

    private final MapInputBoundary interactor;

    public MapController(MapInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String tripId) {

        interactor.selectTrip(new SelectedTripInputData(tripId));
    }
}
