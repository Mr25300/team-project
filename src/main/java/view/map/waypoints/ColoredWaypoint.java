package view.map.waypoints;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;

public class ColoredWaypoint extends DefaultWaypoint {
    private final Color color;

    public ColoredWaypoint(Color color, double latitude, double longitude){
        super(latitude, longitude);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
