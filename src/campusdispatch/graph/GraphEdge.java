package campusdispatch.graph;

/**
 * Represents a road segment between two locations.
 */
public class GraphEdge {
    private final int destinationId;
    private final double weight;
    private final boolean isBidirectional;

    public GraphEdge(int destinationId, double weight, boolean isBidirectional) {
        this.destinationId = destinationId;
        this.weight = weight;
        this.isBidirectional = isBidirectional;
    }

    public int getDestinationId() { return destinationId; }
    public double getWeight() { return weight; }
    public boolean isBidirectional() { return isBidirectional; }

    @Override
    public String toString() {
        return "GraphEdge{dest=" + destinationId + ", weight=" + weight + "}";
    }
}
