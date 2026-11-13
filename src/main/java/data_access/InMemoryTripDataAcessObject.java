package data_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import entity.Photo;
import entity.Trip;
import use_case.list_trips.ListTripsDataAccessInterface;

/**
 * In-memory storage for trips and photos. Implements each use case's
 * data-access interface as those use cases are added.
 */
public class InMemoryTripDataAcessObject implements ListTripsDataAccessInterface {
    private final Map<String, Trip> trips = new LinkedHashMap<>();
    private final Map<String, Photo> photos = new HashMap<>();
    private final Map<String, String> photoPaths = new HashMap<>();

    public Optional<Trip> getTrip(String tripId) {
        return Optional.ofNullable(trips.get(tripId));
    }

    public List<Trip> getAllTrips() {
        return new ArrayList<>(trips.values());
    }

    public void saveTrip(Trip trip) {
        trips.put(trip.getId(), trip);
    }

    public void deleteTrip(String tripId) {
        trips.remove(tripId);
    }

    public Optional<Photo> getPhoto(String photoId) {
        return Optional.ofNullable(photos.get(photoId));
    }

    public void savePhoto(Photo photo, String filePath) {
        photos.put(photo.getId(), photo);
        photoPaths.put(photo.getId(), filePath);
    }

    public String getPhotoPath(String photoId) {
        return photoPaths.get(photoId);
    }

    public List<Photo> getTripPhotos(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip == null) {
            return new ArrayList<>();
        }
        return trip.getPhotoIds().stream()
                .map(photos::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
