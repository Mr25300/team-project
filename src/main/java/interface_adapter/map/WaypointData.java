package interface_adapter.map;

import java.awt.Color;

public record WaypointData(Color color,String tripId, String photoId, double latitude, double longitude) {
}
