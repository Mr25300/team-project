package entity;

public class Location {
    private static final double EARTH_RADIUS = 6371;

    private final double latitude;
    private final double longitude;
    private final LocationInfo info;

    public Location(double latitude, double longitude, LocationInfo info) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.info = info;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LocationInfo getInfo() {
        return info;
    }

    private double[] getUnitVector() {
        double theta = longitude / 180 * Math.PI;
        double phi = (90 - latitude) / 180 * Math.PI;

        return new double[]{
            Math.cos(theta) * Math.sin(phi),
            Math.sin(theta) * Math.sin(phi),
            Math.cos(phi)
        };
    }

    public double getDistanceFrom(Location other) {
        double[] vector1 = getUnitVector();
        double[] vector2 = other.getUnitVector();
        double dotProduct = 0;

        for (int i = 0; i < 3; i++) {
            dotProduct += vector1[i] * vector2[i];
        }

        double angle = Math.acos(Math.max(-1.0, Math.min(1.0, dotProduct)));

        return Location.EARTH_RADIUS * angle;
    }
}
