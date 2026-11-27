package interface_adapter.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.jxmapviewer.viewer.Waypoint;
import view.map.waypoints.ColoredWaypoint;

public class MapState {

    private Set<ColoredWaypoint> waypoints = new HashSet<>();

    public Set<ColoredWaypoint> getWaypoints() {
        return waypoints;
    }
    public void setWaypoints(Set<ColoredWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

}
