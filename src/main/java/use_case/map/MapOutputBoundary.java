package use_case.map;

import entity.Location;
import entity.Trip;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface MapOutputBoundary {
    void present(MapOutputData data);
}
