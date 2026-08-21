package campusdispatch.graph;

import campusdispatch.datastructures.CustomSinglyLinkedList;

/**
 * Represents a location node in the graph.
 */
public class GraphNode {
    private final int locationId;
    private final String name;
    private final CustomSinglyLinkedList adjacencyList;

    public GraphNode(int locationId, String name) {
        this.locationId = locationId;
        this.name = name;
        this.adjacencyList = new CustomSinglyLinkedList();
    }

    public int getLocationId() { return locationId; }
    public String getName() { return name; }

    public void addEdge(GraphEdge edge) {
        adjacencyList.addLast(edge);
    }

    public CustomSinglyLinkedList getEdges() {
        return adjacencyList;
    }

    @Override
    public String toString() {
        return "GraphNode{id=" + locationId + ", name='" + name + "'}";
    }
}
