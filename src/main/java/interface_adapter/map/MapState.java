package interface_adapter.map;

import java.util.List;

public class MapState {

    private List<WaypointData> waypoints;
    private List<TripOption> tripOptions;
    private TripOption selectedTripOption;
    private SelectedLocation selectedLocation;
    private String errorMessage;

    public List<WaypointData> getWaypoints(){
        return waypoints;
    }

    public void setWaypoints(List<WaypointData> waypoints) {
        this.waypoints = waypoints;
    }

    public List<TripOption> getTripOptions() {
        return tripOptions;
    }

    public void setTripOptions(List<TripOption> tripOptions) {
        this.tripOptions = tripOptions;
    }

    public TripOption getSelectedTripOption() {
        return selectedTripOption;
    }

    public void setSelectedTripOption(TripOption selectedTripOption) {
        this.selectedTripOption = selectedTripOption;
    }

    public SelectedLocation getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(SelectedLocation selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
