package use_case.map.output_data;

import java.util.List;

public class MapOutputData {
    private final List<TripPoint> tripPoints;
    private final List<TripSummary> summaries;
    private final TripSummary selectedTrip;

    public MapOutputData(List<TripSummary> summaries, List<TripPoint> tripPoints, TripSummary selectedTrip){
        this.tripPoints = tripPoints;
        this.summaries = summaries;
        this.selectedTrip = selectedTrip;
    }

    public List<TripPoint> getTripPoints() {
        return tripPoints;
    }

    public List<TripSummary> getSummaries() {
        return summaries;
    }

    public TripSummary getSelectedTrip() {
        return selectedTrip;
    }
}
