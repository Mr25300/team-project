package exception;

import java.lang.RuntimeException;

public class ReverseGeocodeException extends RuntimeException {
    public LocationServiceException(String message) {
        super(message);
    }

    public LocationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
