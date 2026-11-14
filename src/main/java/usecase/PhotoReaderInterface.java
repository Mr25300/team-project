package usecase;

import java.io.InputStream;

import entity.Photo;

public interface PhotoReaderInterface {
    InputStream readFullImage(Photo photo);
}
