package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationTest {
    private static final double TOLERANCE_KM = 5.0;

    private final Location toronto = new Location(43.6532, -79.3832, null);
    private final Location montreal = new Location(45.5017, -73.5673, null);
    private final Location tokyo = new Location(35.6762, 139.6503, null);

    @Test 
    void shortDistanceIsCorrect() {
        assertEquals(504.3, toronto.getDistanceFrom(montreal), TOLERANCE_KM);
    }

    @Test
    void longDistanceOver90DegreesIsCorrect() {
        assertEquals(10351.5, toronto.getDistanceFrom(tokyo), TOLERANCE_KM);
    }

    @Test
    void antipodalPointsAreHalfTheCircumference() {
        Location a = new Location(0, 0, null);
        Location b = new Location(0, 180, null);
        assertEquals(Math.PI * 6371, a.getDistanceFrom(b), TOLERANCE_KM);
    }

    @Test 
    void distanceToSelfIsZero() {
        assertEquals(0.0, toronto.getDistanceFrom(toronto), 1e-6);
    }

    @Test
    void distanceIsSymmetric() {
        assertEquals(toronto.getDistanceFrom(tokyo), tokyo.getDistanceFrom(toronto), 1e-9);
    }
}