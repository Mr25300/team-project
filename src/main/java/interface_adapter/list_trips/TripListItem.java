package interface_adapter.list_trips;

/** One row of the trip list, formatted for display. */
public class TripListItem {
    private final String tripId;
    private final String name;
    private final String dateRange;
    private final String details;
    private final String coverPhotoPath;

    public TripListItem(String tripId, String name, String dateRange, String details, String coverPhotoPath) {
        this.tripId = tripId;
        this.name = name;
        this.dateRange = dateRange;
        this.details = details;
        this.coverPhotoPath = coverPhotoPath;
    }

    public String getTripId() { return tripId; }
    public String getName() { return name; }
    public String getDateRange() { return dateRange; }
    public String getDetails() { return details; }
    public String getCoverPhotoPath() { return coverPhotoPath; }
}