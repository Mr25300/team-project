package archive;

import java.nio.file.Path;

import entity.Photo;

public interface ArchiveManagerInterface {
    Path getDirectory();
    Path getPhotoPath(Photo photo);

    void setDirectory(Path newDirectory);
}
