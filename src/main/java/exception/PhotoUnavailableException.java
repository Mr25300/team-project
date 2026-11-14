package exception;

public class PhotoUnavailableException extends RuntimeException {
    private final String photoId;

    public PhotoUnavailableException(String photoId, String reason, Throwable cause) {
        super("Photo with id " + photoId + " unavailable: " + reason, cause);

        this.photoId = photoId;
    }

    public PhotoUnavailableException(String photoId, String reason) {
        this(photoId, reason, null);
    }

    public String getPhotoId() {
        return photoId;
    }
}
