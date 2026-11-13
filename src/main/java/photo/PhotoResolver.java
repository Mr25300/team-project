package photo;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import archive.ArchiveManagerInterface;
import entity.ImageType;
import entity.Photo;

public class PhotoResolver {
    private static final String PHOTOS_FOLDER_NAME = "photos";

    private static final Map<String, ImageType> EXTENSION_TO_TYPE_MAP = Map.of(
        "jpg", ImageType.JPEG,
        "jpeg", ImageType.JPEG,
        "png", ImageType.PNG,
        "webp", ImageType.WEBP
    );

    private static final Map<ImageType, String> TYPE_TO_EXTENSION_MAP = Map.of(
        ImageType.JPEG, "jpg",
        ImageType.PNG, "png",
        ImageType.WEBP, "webp"
    );

    private final ArchiveManagerInterface archiveManager;

    public PhotoResolver(ArchiveManagerInterface archiveManager) {
        this.archiveManager = archiveManager;
    }

    public static Optional<ImageType> getTypeFromExtension(String extension) {
        return Optional.ofNullable(EXTENSION_TO_TYPE_MAP.get(extension.toLowerCase()));
    }

    public static String getExtensionFromType(ImageType type) {
        return TYPE_TO_EXTENSION_MAP.get(type);
    }

    public Path getPhotoPath(Photo photo) {
        String fileName = photo.getId() + '.' + getExtensionFromType(photo.getType());

        return archiveManager.getDirectory()
            .resolve(PHOTOS_FOLDER_NAME)
            .resolve(fileName);
    }
}
