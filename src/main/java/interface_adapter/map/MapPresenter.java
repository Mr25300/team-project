package interface_adapter.map;

import entity.Location;
import java.util.ArrayList;
import java.util.HashSet;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.Waypoint;
import use_case.map.MapOutputBoundary;

public class MapPresenter implements MapOutputBoundary {

    private final MapViewModel viewModel;

    public MapPresenter(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareWaypoints(ArrayList<Location> locations) {
        HashSet<Waypoint> waypoints = new HashSet<>();

        for (Location location : locations){
            Waypoint waypoint = new DefaultWaypoint(location.getLatitude(),location.getLongitude());
            waypoints.add(waypoint);
            System.out.println(waypoint.getPosition().toString());
        }
        viewModel.getState().setWaypoints(waypoints);
    }
}
