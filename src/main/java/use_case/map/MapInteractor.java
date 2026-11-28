package use_case.map;

import entity.Location;
import entity.Photo;
import entity.Trip;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.management.timer.TimerNotification;
import use_case.PhotoAccessException;
import use_case.PhotoAccessInterface;
import use_case.TripAccessException;
import use_case.TripAccessInterface;
import use_case.map.input_data.SelectWaypointInputData;
import use_case.map.input_data.SelectedTripInputData;
import use_case.map.output_data.LocationDetailsOutputData;
import use_case.map.output_data.MapOutputData;
import use_case.map.output_data.TripPoint;
import use_case.map.output_data.TripSummary;

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
    public void selectTrip(SelectedTripInputData input){
        if (input.tripId() == null) {
            showAllTrips();
        } else {
            showTrip(input.tripId());

        }
    }

    @Override
    public void selectWaypoint(SelectWaypointInputData input) {
        Trip trip = tripDataAccess.getTripById(input.photoId());
        Photo photo = photoDataAccess.getPhotoById(input.tripId());

        presenter.presentLocationDetails(new LocationDetailsOutputData(input.photoId(),
                trip.getName(),
                photo.getLocation().getLatitude(),
                photo.getLocation().getLongitude(),
                photo.getLocation().getInfo().getCountry(),
                photo.getTime()
                ));
    }

    private Collection<Location> getLocationsFromTrip(Trip trip) {
        List<Photo> photos = photoDataAccess.getPhotosByTripId(trip.getId());
        return photos.stream().map(Photo::getLocation).toList();
    }

    private void showTrip(String selectedTripId) {
        try {
            Trip selectedTrip = tripDataAccess.getTripById(selectedTripId);

            List<Trip> trips = tripDataAccess.getTrips();

            List<TripSummary> summaries = trips.stream()
                    .map(trip -> new TripSummary(trip.getId(), trip.getName()))
                    .toList();

            List<TripPoint> points = pointsOf(selectedTrip);

            presenter.presentMap(new MapOutputData(summaries, points, new TripSummary(selectedTrip.getId(),selectedTrip.getName())));
        } catch (TripAccessException | PhotoAccessException e) {
            presenter.presentError("Could not load the map: " + e.getMessage());
        }
    }

    private void showAllTrips() {
        try {

            List<Trip> trips = tripDataAccess.getTrips();

            List<TripSummary> summaries = trips.stream()
                    .map(trip -> new TripSummary(trip.getId(), trip.getName()))
                    .toList();

            List<TripPoint> points = trips.stream().flatMap(trip -> pointsOf(trip).stream())
                    .toList();

            presenter.presentMap(new MapOutputData(summaries, points, new TripSummary(null,"All")));
        } catch (TripAccessException | PhotoAccessException e) {
            presenter.presentError("Could not load the map: " + e.getMessage());
        }
    }

    private List<TripPoint> pointsOf(Trip trip) {
        return photoDataAccess.getPhotosByTripId(trip.getId()).stream()
                .filter(photo -> photo.getLocation() != null)
                .map(photo -> new TripPoint(
                        trip.getId(),
                        photo.getId(),
                        photo.getLocation().getLatitude(),
                        photo.getLocation().getLongitude()))
                .toList();
    }
}
