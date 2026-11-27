package interface_adapter.map;

import java.util.ArrayList;
import java.util.HashSet;
import org.jxmapviewer.viewer.Waypoint;
import view.map.waypoints.ColoredWaypoint;

public class MapState {

    private HashSet<ColoredWaypoint> waypoints = new HashSet<>();

    public HashSet<ColoredWaypoint> getWaypoints() {
        return waypoints;
    }
    public void setWaypoints(HashSet<ColoredWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

}
