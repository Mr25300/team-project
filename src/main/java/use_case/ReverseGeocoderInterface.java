package use_case;

import entity.LocationInfo;
import exception.ReverseGeocodeException;

public interface ReverseGeocoderInterface {
    LocationInfo getLocationInfo(double latitude, double longitude) throws ReverseGeocodeException;
}
