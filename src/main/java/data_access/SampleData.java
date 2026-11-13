package data_access;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import entity.Location;
import entity.LocationInfo;
import entity.Photo;
import entity.Trip;

/** Seeds a DAO with demo trips so GUI doesnt depend on Import */
public final class SampleData {
    private static final String[] IMAGES = {"sample1.jpg", "sample2.jpg", "sample3.jpg"};
    private static int photoCounter = 0;

    private SampleData() {}

    public static void populate(InMemoryTripDataAcessObject dao) {
        addTrip(dao, "trip-montreal", "Montreal Weekend",
                LocalDateTime.of(2026, 11, 12, 10, 0), 5,
                45.5017, -73.5673, new LocationInfo("Montreal", null, "Quebec", "Canada"));
        addTrip(dao, "trip-tokyo", "Tokyo",
                LocalDateTime.of(2025, 11, 12, 10, 0), 6,
                35.6762, 139.6503, new LocationInfo("Tokyo", null, "Tokyo", "Japan"));
        addTrip(dao, "trip-vancouver", "Vancouver",
                LocalDateTime.of(2025, 06, 10, 10, 0), 4,
                49.2827, -123.1207, new LocationInfo("Vancouver", null, "British Columbia", "Canada"));
    }

    private static void addTrip(InMemoryTripDataAcessObject dao, String id, String name,
                                LocalDateTime start, int photoCount,
                                double lat, double lon, LocationInfo info) {
        Trip trip = new Trip(id, name, null, null);
        
        for (int i = 0; i < photoCount; i++) {
            // Spread photos ~ every 7 hours and a few hundred meters apart
            Location loc = new Location(lat + i * 0.003, lon + i * 0.004, info);
            Photo photo = new Photo("photo-" + (++photoCounter), loc, start.plusHours(i * 7L));

            dao.savePhoto(photo, resourcePath(IMAGES[i % IMAGES.length]));
            trip.addPhoto(photo);
        }
        dao.saveTrip(trip);
    }

    public static String resourcePath(String fileName) {
        URL url = SampleData.class.getResource("/sample/" + fileName);
        if (url == null) {
            return null;    // missing image -> view shows placeholder
        }
        try {
            return Paths.get(url.toURI()).toString();
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
