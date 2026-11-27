package use_case.map;

import entity.Location;
import entity.Trip;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface MapOutputBoundary {
    void prepareWaypoints(Collection<Location> locations);
    void prepareMultipleTripWaypoints(Map<Trip,Collection<Location>> tripLocations);
}
