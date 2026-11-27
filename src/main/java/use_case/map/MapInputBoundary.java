package use_case.map;

import use_case.map.input_data.FilterTripsInputData;
import use_case.map.input_data.SelectWaypointInputData;

public interface MapInputBoundary {

    void loadMap();
    void filterTrips(FilterTripsInputData input);
    void selectWaypoint(SelectWaypointInputData input);
}
