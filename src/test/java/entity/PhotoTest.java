package entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {
    private final Photo early = new Photo("a", null, LocalDateTime.of(2026, 11, 12, 22, 0));
    private final Photo late = new Photo("b", null, LocalDateTime.of(2026, 11, 13, 22, 0));
    private final Photo noTime = new Photo("c", null, null);

    @Test
    void ordersByTime() {
        assertTrue(early.compareTo(late) < 0);
        assertTrue(late.compareTo(early) > 0);
    }

    @Test
    void nullTimeSortsLastAndDoesNotThrow() {
        List<Photo> photos = new ArrayList<>(List.of(noTime, late, early));
        Collections.sort(photos);
        assertEquals(List.of(early, late, noTime), photos);
    }

    @Test
    void twoNullTimesCompareEqual() {
        assertEquals(0, noTime.compareTo(new Photo("d", null, null)));
    }
}