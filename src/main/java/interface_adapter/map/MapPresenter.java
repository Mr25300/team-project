package interface_adapter.map;

import java.awt.Color;

import use_case.map.LocationDetailsOutputData;
import use_case.map.MapOutputBoundary;
import use_case.map.output_data.MapOutputData;

public class MapPresenter implements MapOutputBoundary {

    private final MapViewModel viewModel;

    public MapPresenter(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentMap(MapOutputData data) {

    }

    @Override
    public void presentLocationDetails(LocationDetailsOutputData data) {

    }

    @Override
    public void presentError(String message) {

    }

    private Color colorFromTripId(String tripId){
        int hash = tripId.hashCode();

        float hue = Math.floorMod(hash,360) / 360.0f;
        float saturation = 0.65f;
        float brightness = 0.85f;

        return Color.getHSBColor(hue,saturation,brightness);

    }
}
