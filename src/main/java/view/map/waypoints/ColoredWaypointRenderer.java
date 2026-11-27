package view.map.waypoints;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import java.awt.*;
import java.awt.geom.Point2D;

public class ColoredWaypointRenderer implements WaypointRenderer<ColoredWaypoint> {
    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, ColoredWaypoint waypoint) {
        Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());

        int x = (int)point.getX() -8 / 2;
        int y = (int)point.getY() -8;

        g = (Graphics2D) g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(waypoint.getColor());
        g.fillOval(x - 8, y - 8, 16, 16);
        g.setColor(Color.BLACK);
        g.drawOval(x - 8, y - 8, 16, 16);
    }
}
