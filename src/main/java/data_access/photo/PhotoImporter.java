package data_access.photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import entity.Photo;
import entity.ImageType;
import entity.Location;
import exception.PhotoImportException;
import use_case.PhotoImporterInterface;

public class PhotoImporter implements PhotoImporterInterface {
    private static final List<DateTimeFormatter> FORMATTERS = List.of(
        DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    );

    private final PhotoResolver resolver;

    public PhotoImporter(PhotoResolver resolver) {
        this.resolver = resolver;
    }

    private static Optional<Photo> getPhotoFromPath(Path imagePath) {
        Optional<ImageType> type = PhotoResolver.getImageType(imagePath);
        if (type.isEmpty()) return Optional.empty();

        try {
            File imageFile = imagePath.toFile();
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

            Location location = null;
            LocalDateTime time = null;

            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (gpsDirectory != null && gpsDirectory.getGeoLocation() != null) {
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();

                if (!geoLocation.isZero()) {
                    location = new Location(geoLocation.getLatitude(), geoLocation.getLongitude(), null);
                }
            }

            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (exifDirectory != null) {
                String dateTimeString = exifDirectory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

                for (DateTimeFormatter formatter : FORMATTERS) {
                    try {
                        time = LocalDateTime.parse(dateTimeString, formatter);
                        break;
                    } catch (DateTimeParseException e) {}
                }
            }

            String id = UUID.randomUUID().toString();
            Photo photo = new Photo(id, type.get(), location, time);

            return Optional.of(photo);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean saveImage(Photo photo, Path imagePath) {
        try {
            Path copyPath = resolver.getPhotoPath(photo);

            Files.createDirectories(copyPath.getParent());
            Files.copy(imagePath, copyPath, StandardCopyOption.REPLACE_EXISTING);

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public void importImages(List<Path> locations, Consumer<Photo> onPhotoProcessed) {
        List<Path> imagePaths = new ArrayList<>();

        for (Path location : locations) {
            try {
                if (Files.isDirectory(location)) {
                    try (Stream<Path> pathStream = Files.walk(location)) {
                        pathStream
                            .filter(path -> Files.isRegularFile(path) && PhotoResolver.isSupportedType(path))
                            .forEach(path -> imagePaths.add(path));
                    }

                } else if (Files.isRegularFile(location) && PhotoResolver.isSupportedType(location)) {
                    imagePaths.add(location);
                }

            } catch (IOException e) {
                throw new PhotoImportException(location, e.getMessage(), e);
            }
        }

        for (Path imagePath : imagePaths) {
            Optional<Photo> photo = getPhotoFromPath(imagePath);
            if (photo.isEmpty()) continue;

            boolean successful = saveImage(photo.get(), imagePath);

            if (successful) {
                onPhotoProcessed.accept(photo.get());
            }
        }
    }
}
