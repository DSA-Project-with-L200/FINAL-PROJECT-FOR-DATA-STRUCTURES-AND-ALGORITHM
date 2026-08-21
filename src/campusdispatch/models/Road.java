package campusdispatch.models;

/**
 * Represents a road segment between two locations on the campus.
 */
public class Road {
    private int roadId;
    private int sourceLocationId;
    private int destLocationId;
    private double distanceMeters;
    private double congestionFactor;
    private boolean isBidirectional;

    /**
     * Constructs a new Road segment.
     * 
     * @param roadId Unique ID for this road.
     * @param sourceLocationId The starting location ID.
     * @param destLocationId The ending location ID.
     * @param distanceMeters The physical distance of the road in meters.
     * @param congestionFactor Multiplier representing current traffic (1.0 = normal).
     * @param isBidirectional Whether traffic can flow in both directions.
     */
    public Road(int roadId, int sourceLocationId, int destLocationId, double distanceMeters, 
                double congestionFactor, boolean isBidirectional) {
        this.roadId = roadId;
        this.sourceLocationId = sourceLocationId;
        this.destLocationId = destLocationId;
        this.distanceMeters = distanceMeters;
        this.congestionFactor = congestionFactor;
        this.isBidirectional = isBidirectional;
    }

    public int getRoadId() { return roadId; }
    public void setRoadId(int roadId) { this.roadId = roadId; }

    public int getSourceLocationId() { return sourceLocationId; }
    public int getSourceId() { return sourceLocationId; }
    public void setSourceLocationId(int sourceLocationId) { this.sourceLocationId = sourceLocationId; }

    public int getDestLocationId() { return destLocationId; }
    public int getDestinationId() { return destLocationId; }
    public void setDestLocationId(int destLocationId) { this.destLocationId = destLocationId; }

    public double getWeight() { return distanceMeters * congestionFactor; }

    public double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }

    public double getCongestionFactor() { return congestionFactor; }
    public void setCongestionFactor(double congestionFactor) { this.congestionFactor = congestionFactor; }

    public boolean isBidirectional() { return isBidirectional; }
    public void setBidirectional(boolean bidirectional) { isBidirectional = bidirectional; }

    @Override
    public String toString() {
        return "Road{id=" + roadId + ", src=" + sourceLocationId + ", dest=" + destLocationId + 
               ", dist=" + distanceMeters + ", congestion=" + congestionFactor + "}";
    }
}
