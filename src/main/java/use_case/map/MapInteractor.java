package use_case.map;

import entity.Location;
import entity.Photo;
import entity.Trip;
import java.util.ArrayList;
import java.util.List;
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
    public ArrayList<Location> execute() {
        ArrayList<Location> locations = new ArrayList<>();
        List<Trip> tripList = tripDataAccess.getTrips();
        for (Trip trip : tripList){
            List<Photo> photos = photoDataAccess.getPhotosByTripId(trip.getId());
            List<Location> perTripLocations = photos.stream().map(Photo::getLocation).toList();
            locations.addAll(perTripLocations);
        }
        presenter.prepareWaypoints(locations);
        return locations;
    }
}
