package data_access;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import entity.Photo;
import entity.Trip;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTripDataAcessObjectTest {
    private static final LocalDateTime T0 = LocalDateTime.of(2025, 1, 1, 12, 0);
    private InMemoryTripDataAcessObject dao;

    @BeforeEach
    void setUp() {
        dao = new InMemoryTripDataAcessObject();
    }

    @Test
    void saveThenGetTrip() {
        Trip trip = new Trip("t1", "Trip", T0, T0);
        dao.saveTrip(trip);
        assertSame(trip, dao.getTrip("t1").orElseThrow());
    }

    @Test 
    void missingIdsReturnEmpty() {
        assertTrue(dao.getTrip("nope").isEmpty());
        assertTrue(dao.getPhoto("nope").isEmpty());
        assertNull(dao.getPhotoPath("nope"));
        assertTrue(dao.getTripPhotos("nope").isEmpty());
    }

    @Test 
    void deleteTripKeepsPhotos() {
        Photo p = new Photo("p1", null, T0);
        dao.savePhoto(p, "/x.jpg");
        Trip trip = new Trip("t1", "Trip", null, null);
        trip.addPhoto(p);
        dao.saveTrip(trip);

        dao.deleteTrip("t1");

        assertTrue(dao.getTrip("t1").isEmpty());
        assertTrue(dao.getPhoto("p1").isPresent());
    }

    @Test
    void getAllTripsReturnsCopyInInsertionOrder() {
        dao.saveTrip(new Trip("a", "A", T0, T0));
        dao.saveTrip(new Trip("b", "B", T0, T0));

        List<Trip> all = dao.getAllTrips();
        assertEquals("a", all.get(0).getId());
        assertEquals("b", all.get(1).getId());

        all.clear();
        assertEquals(2, dao.getAllTrips().size());
    }

    @Test 
    void getTripPhotosSkipsUnknownIds() {
        Photo known = new Photo("p1", null, T0);
        dao.savePhoto(known, "/x.jpg");
        Trip trip = new Trip("t1", "Trip", null, null);
        trip.addPhoto(known);
        trip.addPhoto(new Photo("ghost", null, T0));
        dao.saveTrip(trip);

        assertEquals(List.of(known), dao.getTripPhotos("t1"));
    }

    @Test 
    void sampleDataBuildsConsistentTrips() {
        SampleData.populate(dao);

        assertEquals(3, dao.getAllTrips().size());
        for (Trip trip : dao.getAllTrips()) {
            List<Photo> photos = dao.getTripPhotos(trip.getId());
            assertFalse(photos.isEmpty());
            assertEquals(photos.get(0).getTime(), trip.getStartTime());
            assertEquals(photos.get(photos.size() - 1).getTime(), trip.getEndTime());
        }
    }
}