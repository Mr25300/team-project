package interface_adapter.map;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;
import use_case.map.output_data.LocationDetailsOutputData;
import use_case.map.MapOutputBoundary;
import use_case.map.output_data.MapOutputData;
import use_case.map.output_data.TripPoint;
import use_case.map.output_data.TripSummary;

public class MapPresenter implements MapOutputBoundary {

    private final MapViewModel viewModel;

    public MapPresenter(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentMap(MapOutputData data) {
        prepareWaypoints(data.getTripPoints());
        prepareTripSelection(data.getSummaries());
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentLocationDetails(LocationDetailsOutputData data) {

    }

    @Override
    public void presentError(String message) {

    }

    private void prepareTripSelection(List<TripSummary> tripSummaries){
        List<TripOption> tripOptions = new ArrayList<>();
        tripOptions.add(new TripOption(null,"All"));
        List<TripOption> fetchedTripOptions = tripSummaries.stream().map(tripSummary -> new TripOption(tripSummary.id(),tripSummary.name())).toList();
        tripOptions.addAll(fetchedTripOptions);
        viewModel.getState().setTripOptions(tripOptions);
    }

    private void prepareWaypoints(List<TripPoint> tripPoints){
        List<WaypointData> waypoints = tripPoints.stream().map(tripPoint ->
                new WaypointData( colorFromTripId(tripPoint.tripId()),
                tripPoint.photoId(),
                tripPoint.tripId(),
                tripPoint.latitude(),
                tripPoint.longitude())).toList();

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
