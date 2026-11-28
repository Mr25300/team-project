package use_case.map;

import use_case.map.input_data.SelectWaypointInputData;
import use_case.map.input_data.SelectedTripInputData;

public interface MapInputBoundary {
    void selectTrip(SelectedTripInputData input);
    void selectWaypoint(SelectWaypointInputData input);
}
