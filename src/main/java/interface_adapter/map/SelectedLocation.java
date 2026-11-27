package interface_adapter.map;

import java.time.LocalDateTime;

public record SelectedLocation(String tripName, double latitude, double longitude, String placeName, LocalDateTime time) {
}
