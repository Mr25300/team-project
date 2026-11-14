package entity;

import java.util.Comparator;
import java.time.LocalDateTime;

public class Photo implements Comparable<Photo> {
    private static final Comparator<Photo> BY_TIME =
        Comparator.comparing(Photo::getTime, Comparator.nullsLast(Comparator.naturalOrder()));

    private final String id;
    private final ImageType type;
    private final Location location;
    private final LocalDateTime time;

    public Photo(String id, ImageType type, Location location, LocalDateTime time) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public ImageType getType() {
        return type;
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
