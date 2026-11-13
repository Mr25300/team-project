package archive;

import java.nio.file.Path;

import entity.Photo;

public class ArchiveManager implements ArchiveManagerInterface {
    private static final String FOLDER_NAME = "journeye";

    private Path directory;

    private static Path getDefaultDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        Path basePath;

        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");

            if (localAppData != null && !localAppData.isBlank()) {
                basePath = Path.of(localAppData);
            } else {
                basePath = Path.of(userHome, "AppData", "Local");
            }

        } else if (os.contains("mac") || os.contains("darwin")) {
            basePath = Path.of(userHome, "Library", "Application Support");

        } else {
            String xdgDataHome = System.getenv("XDG_DATA_HOME");

            if (xdgDataHome != null && !xdgDataHome.isBlank()) {
                basePath = Path.of(xdgDataHome);
            } else {
                basePath = Path.of(userHome, ".local", "share");
            }
        }

        return basePath.resolve(FOLDER_NAME);
    }

    public ArchiveManager() {
        directory = getDefaultDirectory();
    }

    public Path getDirectory() {
        return directory;
    }

    public void setDirectory(Path newDirectory) {
        directory = newDirectory;
    }
}
