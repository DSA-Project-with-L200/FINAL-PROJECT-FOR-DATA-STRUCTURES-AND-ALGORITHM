package campusdispatch.models;

/**
 * Represents a location on the University of Ghana campus.
 * Built from scratch as required, without using external collections.
 */
public class Location implements Comparable<Location> {
    private int locationId;
    private String name;
    private String zone;
    private double latitude;
    private double longitude;

    /**
     * Constructs a new Location.
     * 
     * @param locationId Unique identifier for the location.
     * @param name The name of the location.
     * @param zone The zone or area it belongs to.
     * @param latitude Geographical latitude.
     * @param longitude Geographical longitude.
     */
    public Location(int locationId, String name, String zone, double latitude, double longitude) {
        this.locationId = locationId;
        this.name = name;
        this.zone = zone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocationId() { return locationId; }
    public int getId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * Calculates direct Haversine (great-circle) distance in meters
     * to another Location based on GPS coordinates.
     */
    public double distanceToInMeters(Location other) {
        final double R = 6371000; // Earth radius in meters
        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - this.latitude);
        double deltaLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public int compareTo(Location other) {
        return Integer.compare(this.locationId, other.locationId);
    }

    @Override
    public String toString() {
        return "Location{id=" + locationId + ", name='" + name + "', zone='" + zone + "'}";
    }
}
