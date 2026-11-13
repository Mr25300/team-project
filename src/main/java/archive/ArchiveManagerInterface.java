package archive;

import java.nio.file.Path;

public interface ArchiveManagerInterface {
    Path getDirectory();
    void setDirectory(Path newDirectory);
}
