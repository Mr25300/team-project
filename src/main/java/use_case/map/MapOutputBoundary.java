package use_case.map;

import use_case.map.output_data.LocationDetailsOutputData;
import use_case.map.output_data.MapOutputData;

public interface MapOutputBoundary {
    void presentMap(MapOutputData data);                          // load + filter
    void presentLocationDetails(LocationDetailsOutputData data);  // waypoint click
    void presentError(String message);
}
