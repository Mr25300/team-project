package photo;

import java.nio.file.Path;
import java.util.function.Consumer;

import entity.Photo;

public interface PhotoImporterInterface {
    void importImages(Path sourceDirectory, Consumer<Photo> onPhotoProcessed);
}
