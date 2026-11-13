package photo;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

import archive.ArchiveManagerInterface;
import entity.Photo;
import entity.Location;

public class PhotoImporter implements PhotoImporterInterface {
    private static final List<String> SUPPORTED_TYPES = List.of(
        "jpg", "png"
    );

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
        DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    );

    private ArchiveManagerInterface archiveManager;

    public PhotoImporter(ArchiveManagerInterface archiveManager) {
        this.archiveManager = archiveManager;
    }

    private static boolean isSupportedImage(Path path) {
        if (!Files.isRegularFile(path)) {
            return false;
        }

        String name = path.getFileName().toString().toLowerCase();

        return SUPPORTED_TYPES.stream().anyMatch(type -> name.endsWith("." + type));
    }

    private static Photo getImageMetadata(File imageFile) {
        try {
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

            return new Photo(UUID.randomUUID().toString(), location, time);

        } catch (Exception e) {
            return null; 
        }
    }

    private void writeImage(Photo photo, Path imagePath) {
        try {
            Path archiveDir = archiveManager.getDirectory();
            Path photosDir = archiveDir.resolve("photos");

            String sourceFileName = imagePath.getFileName().toString();
            String extension = "";
            int dotIndex = sourceFileName.lastIndexOf('.');

            if (dotIndex > 0 && dotIndex < sourceFileName.length() - 1) {
                extension = sourceFileName.substring(dotIndex + 1);
            }

            String targetFileName = photo.getId() + (extension.isEmpty() ? "" : "." + extension);
            Path destinationPath = photosDir.resolve(targetFileName);

            Files.createDirectories(photosDir);
            Files.copy(imagePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new PhotoHandlerException("Failed to copy image to archive", e);
        }
    }

    public void importImages(Path sourceDirectory, Consumer<Photo> onPhotoProcessed) {
        try (Stream<Path> pathStream = Files.walk(sourceDirectory)) {
            pathStream.filter(PhotoImporter::isSupportedImage)
                .forEach(path -> {
                    Photo photo = getImageMetadata(path.toFile());

                    writeImage(photo, path);

                    onPhotoProcessed.accept(photo);
                });

        } catch (IOException e) {
            throw new PhotoHandlerException("Failed to stream photos from directory", e);
        }
    }
}
