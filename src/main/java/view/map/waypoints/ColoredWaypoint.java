package view.map.waypoints;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;

public class ColoredWaypoint extends DefaultWaypoint {
    private final Color color;
    private final String tripId;
    private final String photoId;

    public ColoredWaypoint(Color color, String tripId, String photoId, double latitude, double longitude){
        super(latitude, longitude);
        this.color = color;
        this.tripId = tripId;
        this.photoId = photoId;
    }

    public Color getColor() {
        return color;
    }

    public String getTripId() {
        return tripId;
    }

    public String getPhotoId() {
        return photoId;
    }
}
