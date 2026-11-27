package use_case.map;

import entity.Location;
import entity.Trip;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MapOutputData {
    private final List<String> tripIds;
    private final Map<Trip, Collection<Location>> tripLocations;

    public MapOutputData(List<String> tripIds, Map<Trip, Collection<Location>> tripLocations){
        this.tripIds = tripIds;
        this.tripLocations = tripLocations;
    }

    public List<String> getTripIds() {
        return tripIds;
    }

    public Map<Trip, Collection<Location>> getTripLocations(){
        return tripLocations;
    }
}
