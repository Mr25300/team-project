package interface_adapter.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jxmapviewer.viewer.Waypoint;
import view.map.waypoints.ColoredWaypoint;

public class MapState {

    private Set<ColoredWaypoint> waypoints = new HashSet<>();
    private List<String> tripCatalog = new ArrayList<>();


    public Set<ColoredWaypoint> getWaypoints() {
        return waypoints;
    }
    public void setWaypoints(Set<ColoredWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public List<String> getTripCatalog(){
        return tripCatalog;
    }
    public void setTripCatalog(List<String> tripCatalog){
        this.tripCatalog = tripCatalog;
    }

}
