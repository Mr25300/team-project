package use_case.list_trips;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Location;
import entity.LocationInfo;
import entity.Photo;
import entity.Trip;

import static org.junit.jupiter.api.Assertions.*;

class ListTripsInteractorTest {

    static class FakeDataAccess implements ListTripsDataAccessInterface {
        final List<Trip> trips = new ArrayList<>();
        final Map<String, Photo> photos = new HashMap<>();
        final Map<String, String> paths = new HashMap<>();
        boolean fail = false;

        void addTrip(Trip trip, Photo... tripPhotos) {
            for (Photo p : tripPhotos) {
                photos.put(p.getId(), p);
                paths.put(p.getId(), "/photos/" + p.getId() + ".jpg");
                trip.addPhoto(p);
            }
            trips.add(trip);
        }

        @Override
        public List<Trip> getAllTrips() {
            if (fail) {
                throw new IllegalStateException("disk on fire");
            }
            return new ArrayList<>(trips);
        }

        @Override
        public List<Photo> getTripPhotos(String tripId) {
            List<Photo> result = new ArrayList<>();
            for (Trip t : trips) {
                if (t.getId().equals(tripId)) {
                    t.getPhotoIds().forEach(id -> result.add(photos.get(id)));
                }
            }
            return result;
        }

        @Override
        public String getPhotoPath(String photoId) {
            return paths.get(photoId);
        }
    }

    /** Records what the interactor sent, instead of updating a real ViewModel. */
    static class CapturingPresenter implements ListTripsOutputBoundary {
        ListTripsOutputData success;
        String failure;

        @Override
        public void prepareSuccessView(ListTripsOutputData outputData) { success = outputData; }

        @Override
        public void prepareFailView(String errorMessage) { failure = errorMessage; }
    }

    private static final LocationInfo MONTREAL = new LocationInfo("Montreal", null, "Quebec", "Canada");
    private static final LocationInfo QUEBEC = new LocationInfo("Quebec City", null, "Quebec", "Canada");

    private FakeDataAccess dao;
    private CapturingPresenter presenter;
    private ListTripsInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new FakeDataAccess();
        presenter = new CapturingPresenter();
        interactor = new ListTripsInteractor(dao, presenter);
    }

    private static Photo photo(String id, LocalDateTime time, LocationInfo info) {
        return new Photo(id, info == null ? null : new Location(45, -73, info), time);
    }

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2025, 5, d, 12, 0);
    }

    @Test
    void tripsAreSortedNewestFirst() {
        dao.addTrip(new Trip("old", "Old", null, null), photo("a", day(1), MONTREAL));
        dao.addTrip(new Trip("new", "New", null, null), photo("b", day(20), MONTREAL));
        dao.addTrip(new Trip("mid", "Mid", null, null), photo("c", day(10), MONTREAL));

        interactor.execute();

        List<String> ids = new ArrayList<>();
        presenter.success.getTrips().forEach(t -> ids.add(t.getId()));
        assertEquals(List.of("new", "mid", "old"), ids);
    }

    @Test
    void tripWithoutDatesGoesLast() {
        dao.addTrip(new Trip("undated", "Undated", null, null), photo("a", null, MONTREAL));
        dao.addTrip(new Trip("dated", "Dated", null, null), photo("b", day(1), MONTREAL));

        interactor.execute();

        assertEquals("undated", presenter.success.getTrips().get(1).getId());
    }

    @Test
    void summaryHasCountsCoverAndMostCommonLocation() {
        dao.addTrip(new Trip("t", "Trip", null, null),
                photo("late", day(3), QUEBEC),
                photo("early", day(1), MONTREAL),
                photo("mid", day(2), MONTREAL));

        interactor.execute();

        TripSummary summary = presenter.success.getTrips().get(0);
        assertEquals(3, summary.getPhotoCount());
        assertEquals("Montreal, Canada", summary.getLocationLabel());
        assertEquals("/photos/early.jpg", summary.getCoverPhotoPath());   // earliest photo, not first added
        assertEquals(day(1), summary.getStartTime());
        assertEquals(day(3), summary.getEndTime());
    }

    @Test
    void locationTieGoesToEarliestPhoto() {
        dao.addTrip(new Trip("t", "Trip", null, null),
                photo("a", day(1), QUEBEC),
                photo("b", day(2), MONTREAL));

        interactor.execute();

        assertEquals("Quebec City, Canada", presenter.success.getTrips().get(0).getLocationLabel());
    }

    @Test
    void photosWithoutLocationGiveNullLabel() {
        dao.addTrip(new Trip("t", "Trip", null, null), photo("a", day(1), null));

        interactor.execute();

        assertNull(presenter.success.getTrips().get(0).getLocationLabel());
    }

    @Test
    void tripWithNoPhotosHasNoCover() {
        dao.addTrip(new Trip("t", "Empty", day(1), day(1)));

        interactor.execute();

        TripSummary summary = presenter.success.getTrips().get(0);
        assertEquals(0, summary.getPhotoCount());
        assertNull(summary.getCoverPhotoPath());
    }

    @Test
    void emptyArchiveSucceedsWithEmptyList() {
        interactor.execute();

        assertNull(presenter.failure);
        assertTrue(presenter.success.getTrips().isEmpty());
    }

    @Test
    void dataAccessErrorGoesToFailView() {
        dao.fail = true;

        interactor.execute();

        assertNull(presenter.success);
        assertTrue(presenter.failure.contains("disk on fire"));
    }
}