package use_case.map.output_data;

import java.util.List;

public class MapOutputData {
    private final List<TripPoint> tripPoints;

    public MapOutputData(List<TripSummary> summaries, List<TripPoint> tripPoints){
        this.tripPoints = tripPoints;
    }

    public List<TripPoint> getTripIds() {
        return tripPoints;
    }

}
