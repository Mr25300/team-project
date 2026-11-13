package entity;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TripTest {
    private static final LocalDateTime T0 = LocalDateTime.of(2026, 11, 12, 22, 0);

    private Photo photoAt(String id, LocalDateTime time) {
        return new Photo(id, null, time);
    }

    @Test
    void addPhotoWidensRangeBothWays() {
        Trip trip = new Trip("t", "Test", T0, T0);
        trip.addPhoto(photoAt("before", T0.minusDays(1)));
        trip.addPhoto(photoAt("after", T0.plusDays(2)));

        assertEquals(T0.minusDays(1), trip.getStartTime());
        assertEquals(T0.plusDays(2), trip.getEndTime());
        assertEquals(Duration.ofDays(3), trip.getDuration());
    }

    @Test
    void firstPhotosSetsRangeOnEmptyTrip() {
        Trip trip = new Trip("t", "Test", null, null);
        trip.addPhoto(photoAt("p", T0));

        assertEquals(T0, trip.getStartTime());
        assertEquals(T0, trip.getEndTime());
    }

    @Test
    void photoWithoutTimeIsAddedButDoesNotChangeRange() {
        Trip trip = new Trip("t", "Test", T0, T0);
        trip.addPhoto(photoAt("p", null));

        assertEquals(T0, trip.getStartTime());
        assertTrue(trip.getPhotoIds().contains("p"));
    }

    @Test 
    void getPhotoIdsCannotModifyTrip() {
        Trip trip = new Trip("t", "Test", T0, T0);
        assertThrows(UnsupportedOperationException.class,
                () -> trip.getPhotoIds().add("sneaky"));
    }

    @Test
    void setNameChangesName() {
        Trip trip = new Trip("t", "Test", T0, T0);
        trip.setName("new");
        assertEquals("new", trip.getName());
    }
}