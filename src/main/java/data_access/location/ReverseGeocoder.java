package data_access.location;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONException;
import org.json.JSONObject;

import entity.LocationInfo;
import exception.ReverseGeocodeException;
import use_case.ReverseGeocoderInterface;

public class ReverseGeocoder implements ReverseGeocoderInterface {
    private static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/reverse";

    private final HttpClient httpClient;

    public ReverseGeocoder() {
        httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    public LocationInfo getLocationInfo(double latitude, double longitude) throws ReverseGeocodeException {
        String url = String.format("%s?format=json&lat=%f&lon=%f&zoom=10",
            NOMINATIM_BASE_URL, latitude, longitude);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "JourneyeApp/1.0 (Local Desktop App)")
            .header("Accept", "application/json")
            .GET()
            .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new LocationServiceException("OSM Nominatim API failed [Status " + response.statusCode() + "]: " + response.body());
            }

            JSONObject body = new JSONObject(response.body());
            JSONObject address = body.getJSONObject("address");

            return new LocationInfo(
                address.optString("city"),
                address.optString("county"),
                address.optString("state"),
                address.optString("country")
            );

        } catch (IOException e) {
            throw new ReverseGeocodeException(e.getMessage(), e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new ReverseGeocodeException(e.getMessage(), e);

        } catch (JSONException e) {
            throw new ReverseGeocodeException(e.getMessage(), e);
        }
    }
}
