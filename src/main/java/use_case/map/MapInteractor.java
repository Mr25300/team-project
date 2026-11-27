package use_case.map;

import entity.Location;
import entity.LocationInfo;
import entity.Photo;
import entity.Trip;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import use_case.PhotoAccessInterface;
import use_case.TripAccessInterface;

public class MapInteractor implements MapInputBoundary{

    private TripAccessInterface tripDataAccess;
    private PhotoAccessInterface photoDataAccess;
    private MapOutputBoundary presenter;


    public MapInteractor(TripAccessInterface tripDataAccess, PhotoAccessInterface photoDataAccess, MapOutputBoundary presenter) {
        this.tripDataAccess = tripDataAccess;
        this.photoDataAccess = photoDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void updateWaypoints() {
        presenter.prepareMultipleTripWaypoints(getAllTripLocations());
    }

    private Collection<Location> getLocationsFromTrip(Trip trip) {
        List<Photo> photos = photoDataAccess.getPhotosByTripId(trip.getId());
        return photos.stream().map(Photo::getLocation).toList();
    }

    private Map<Trip, Collection<Location>> getAllTripLocations() {
        HashMap<Trip,Collection<Location>> tripLocations = new HashMap<>();
        for (Trip trip : tripDataAccess.getTrips()) {
            tripLocations.put(trip,getLocationsFromTrip(trip));
        }
        return tripLocations;
    }

    private Photo getPhotoFromLocation(Location location) {
        return null;
    }

    private LocationInfo getLocationInfo(Location location) {
        return null;
    }
}
