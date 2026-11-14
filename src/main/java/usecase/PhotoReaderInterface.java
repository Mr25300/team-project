package usecase;

import java.io.InputStream;

import entity.Photo;

public interface PhotoReaderInterface {
    InputStream readThumbnail(Photo photo);
    InputStream readFullImage(Photo photo);
}
