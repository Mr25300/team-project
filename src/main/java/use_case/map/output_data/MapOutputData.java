package use_case.map.output_data;

import java.util.List;

public class MapOutputData {
    private final List<TripPoint> tripPoints;
    private final List<TripSummary> summaries;

    public MapOutputData(List<TripSummary> summaries, List<TripPoint> tripPoints){
        this.tripPoints = tripPoints;
        this.summaries = summaries;
    }

    public List<TripPoint> getTripPoints() {
        return tripPoints;
    }

    public List<TripSummary> getSummaries() {
        return summaries;
    }
}
