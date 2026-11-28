package use_case.map;

import use_case.map.input_data.SelectWaypointInputData;

public interface MapInputBoundary {

    void loadMap();
    void selectWaypoint(SelectWaypointInputData input);
}
