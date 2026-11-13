package use_case.list_trips;

import java.time.LocalDateTime;

/** Read-only facts about one trip, for the list. */
public class TripSummary {
    private final String id;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int photoCount;
    private final String locationLabel;
    private final String coverPhotoPath;

    public TripSummary(String id, String name, LocalDateTime startTime, LocalDateTime endTime,
                       int photoCount, String locationLabel, String coverPhotoPath) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.photoCount = photoCount;
        this.locationLabel = locationLabel;
        this.coverPhotoPath = coverPhotoPath;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getPhotoCount() { return photoCount; }
    /** "City, Country", or null if no photo in the trip has a known location. */
    public String getLocationLabel() { return locationLabel; }
    /** File path of the cover photo, or null if the trip has no photos. */
    public String getCoverPhotoPath() { return coverPhotoPath; }
}