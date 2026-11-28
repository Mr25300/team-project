package interface_adapter.map;

import java.time.LocalDateTime;

public record SelectedLocation(String tripName, double latitude, double longitude,String photoId, String placeName, LocalDateTime time) {
}
