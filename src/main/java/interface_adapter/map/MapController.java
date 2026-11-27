package interface_adapter.map;

import use_case.map.MapInputBoundary;

public class MapController {

    private final MapInputBoundary interactor;

    public MapController(MapInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.updateWaypoints();
    }
}
