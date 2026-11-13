package entity;
import java.util.Comparator;
import java.time.LocalDateTime;

public class Photo implements Comparable<Photo> {
    private static final Comparator<Photo> BY_TIME =
        Comparator.comparing(Photo::getTime, Comparator.nullsLast(Comparator.naturalOrder()));
    
    private String id;
    private Location location;
    private LocalDateTime time;

    public Photo(String id, Location location, LocalDateTime time) {
        this.id = id;
        this.location = location;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public int compareTo(Photo other) {
        return BY_TIME.compare(this, other);
    }
}
