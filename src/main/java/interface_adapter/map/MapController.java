package interface_adapter.map;

import use_case.map.MapInputBoundary;
import use_case.map.input_data.SelectWaypointInputData;
import use_case.map.input_data.SelectedTripInputData;
import view.map.waypoints.ColoredWaypoint;

public class MapController {

    private final MapInputBoundary interactor;

    public MapController(MapInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String tripId) {

        interactor.selectTrip(new SelectedTripInputData(tripId));
    }

    public void selectWaypoint(ColoredWaypoint waypoint){
        interactor.selectWaypoint(new SelectWaypointInputData(waypoint.getTripId(),waypoint.getPhotoId()));
    }
}
