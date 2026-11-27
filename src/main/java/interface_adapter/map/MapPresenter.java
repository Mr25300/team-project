package interface_adapter.map;

import entity.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import use_case.map.MapOutputBoundary;
import view.map.waypoints.ColoredWaypoint;

public class MapPresenter implements MapOutputBoundary {

    private final MapViewModel viewModel;

    public MapPresenter(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareWaypoints(ArrayList<Location> locations) {
        HashSet<ColoredWaypoint> waypoints = new HashSet<>();

        for (Location location : locations) {
            ColoredWaypoint waypoint = new ColoredWaypoint(new Color((int)(Math.random() * 255),
                    (int)(Math.random() * 255),
                    (int)(Math.random() * 255)),
                    new GeoPosition(location.getLatitude(),location.getLongitude()));
            waypoints.add(waypoint);
            System.out.println(waypoint.getPosition().toString());
        }
        viewModel.getState().setWaypoints(waypoints);
        viewModel.firePropertyChanged();
    }
}
