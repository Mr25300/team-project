package interface_adapter.map;

import entity.Location;

import entity.Trip;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import use_case.map.MapOutputBoundary;
import use_case.map.MapOutputData;
import view.map.waypoints.ColoredWaypoint;

public class MapPresenter implements MapOutputBoundary {
    public static final String ALL_TRIPS = "All";

    private final MapViewModel viewModel;

    public MapPresenter(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void present(MapOutputData data) {
        prepareMultipleTripWaypoints(data.getTripLocations());
        prepareTripCatalog(data.getTripIds());
        viewModel.firePropertyChanged();
    }



    private void prepareTripCatalog(List<String> trips){
        List<String> tripCatalog = new ArrayList<>();
        tripCatalog.add(ALL_TRIPS);
        tripCatalog.addAll(trips);
        viewModel.getState().setTripCatalog(tripCatalog);
    }


    private void prepareMultipleTripWaypoints(Map<Trip, Collection<Location>> tripLocations) {
        Set<Trip> trips = tripLocations.keySet();
        Set<ColoredWaypoint> waypoints = new HashSet<>();
        for(Trip trip : trips){
            Color color = colorFromTripId(trip.getId());
            for (Location location : tripLocations.get(trip)){
                ColoredWaypoint waypoint = new ColoredWaypoint(color, location.getLatitude(), location.getLongitude());
                waypoints.add(waypoint);
            }
        }

        viewModel.getState().setWaypoints(waypoints);
    }

    private Color colorFromTripId(String tripId){
        int hash = tripId.hashCode();

        float hue = Math.floorMod(hash,360) / 360.0f;
        float saturation = 0.65f;
        float brightness = 0.85f;

        return Color.getHSBColor(hue,saturation,brightness);

    }
}
