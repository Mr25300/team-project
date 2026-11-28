package use_case.map.output_data;

import java.time.LocalDateTime;

public record LocationDetailsOutputData(
        String photoId,
        String tripName,
        double latitude,
        double longitude,
        String placeName,
        LocalDateTime time){}
