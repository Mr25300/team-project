package photo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import entity.Photo;
import exception.PhotoUnavailableException;
import usecase.PhotoReaderInterface;

public class PhotoReader implements PhotoReaderInterface {
    private final PhotoResolver resolver;

    public PhotoReader(PhotoResolver resolver) {
        this.resolver = resolver;
    }

    public InputStream readFullImage(Photo photo) {
        try {
            Path imagePath = resolver.getPhotoPath(photo);

            return Files.newInputStream(imagePath);

        } catch (IOException e) {
            throw new PhotoUnavailableException(photo.getId(), e.getMessage(), e);
        }
    }
}
