package interface_adapter.map;

import java.util.ArrayList;
import java.util.HashSet;
import org.jxmapviewer.viewer.Waypoint;

public class MapState {

    private HashSet<Waypoint> waypoints = new HashSet<>();

    public HashSet<Waypoint> getWaypoints() {
        return waypoints;
    }
    public void setWaypoints(HashSet<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

}
