package exception;

import java.lang.RuntimeException;
import java.nio.file.Path;

public class PhotoImportException extends RuntimeException {
    private final Path offendingPath;

    public PhotoImportException(Path offendingPath, String reason, Throwable cause) {
        super("Import failed for path \"" + offendingPath + "\": " + reason, cause);

        this.offendingPath = offendingPath;
    }

    public PhotoImportException(Path offendingPath, String reason) {
        this(offendingPath, reason, null);
    }

    public Path getOffendingPath() {
        return offendingPath;
    }
}
