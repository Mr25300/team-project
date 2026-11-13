package use_case.list_trips;

import java.util.List;

import entity.Photo;
import entity.Trip;

/** The data this use case needs. Owned by the use case. Implemented in data_access. */
public interface ListTripsDataAccessInterface {
    List<Trip> getAllTrips();

    List<Photo> getTripPhotos(String tripId);

    String getPhotoPath(String photoId);
}