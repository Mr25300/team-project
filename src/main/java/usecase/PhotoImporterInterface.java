package usecase;

import java.util.List;
import java.nio.file.Path;
import java.util.function.Consumer;

import entity.Photo;

public interface PhotoImporterInterface {
    void importImages(List<Path> directories, Consumer<Photo> onPhotoProcessed);
}
