package use_case.map;

import entity.Location;
import java.util.ArrayList;

public interface MapOutputBoundary {
    void prepareWaypoints(ArrayList<Location> locations);
}
