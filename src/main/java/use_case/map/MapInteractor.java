package use_case.map;

import entity.Location;
import entity.Photo;
import entity.Trip;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import use_case.PhotoAccessException;
import use_case.PhotoAccessInterface;
import use_case.TripAccessException;
import use_case.TripAccessInterface;
import use_case.map.input_data.FilterTripsInputData;
import use_case.map.input_data.SelectWaypointInputData;
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
    public void loadMap() {
        showTrips(Set.of());
    }

    @Override
    public void filterTrips(FilterTripsInputData input) {
        showTrips(input.tripIds());
    }

    @Override
    public void selectWaypoint(SelectWaypointInputData input) {

    }

    private Collection<Location> getLocationsFromTrip(Trip trip) {
        List<Photo> photos = photoDataAccess.getPhotosByTripId(trip.getId());
        return photos.stream().map(Photo::getLocation).toList();
    }

    private void showTrips(Set<String> selectedTripIds) {
        try {
            List<Trip> trips = tripDataAccess.getTrips();

            List<TripSummary> summaries = trips.stream()
                    .map(trip -> new TripSummary(trip.getId(), trip.getName()))
                    .toList();

            List<TripPoint> points = trips.stream()
                    .filter(trip -> selectedTripIds.isEmpty() || selectedTripIds.contains(trip.getId()))
                    .flatMap(trip -> pointsOf(trip).stream())
                    .toList();

            presenter.presentMap(new MapOutputData(summaries, points));
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
