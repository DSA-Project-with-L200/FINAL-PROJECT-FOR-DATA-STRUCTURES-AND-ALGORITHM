package campusdispatch.graph;

import campusdispatch.datastructures.CustomHashTable;
import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.datastructures.CustomSinglyLinkedList;
import campusdispatch.models.Location;
import campusdispatch.models.Road;

/**
 * The campus road network graph.
 */
public class CampusGraph {
    private final CustomHashTable nodes;
    private int nodeCount;
    private int edgeCount;

    public CampusGraph() {
        this.nodes = new CustomHashTable();
        this.nodeCount = 0;
        this.edgeCount = 0;
    }

    public void addNode(Location loc) {
        if (nodes.get(String.valueOf(loc.getId())) == null) {
            nodes.put(String.valueOf(loc.getId()), new GraphNode(loc.getId(), loc.getName()));
            nodeCount++;
        }
    }

    public void addEdge(Road road) {
        GraphNode fromNode = (GraphNode) nodes.get(String.valueOf(road.getSourceId()));
        GraphNode toNode = (GraphNode) nodes.get(String.valueOf(road.getDestinationId()));

        if (fromNode != null && toNode != null) {
            fromNode.addEdge(new GraphEdge(toNode.getLocationId(), road.getWeight(), road.isBidirectional()));
            edgeCount++;
            if (road.isBidirectional()) {
                toNode.addEdge(new GraphEdge(fromNode.getLocationId(), road.getWeight(), road.isBidirectional()));
                edgeCount++;
            }
        }
    }

    public GraphNode getNode(int locationId) {
        return (GraphNode) nodes.get(String.valueOf(locationId));
    }

    public int getNodeCount() { return nodeCount; }
    public int getEdgeCount() { return edgeCount; }

    public double[][] getAdjacencyMatrix() {
        int maxId = nodeCount + 100; // Assumption for matrix size
        double[][] matrix = new double[maxId][maxId];
        for (int i = 0; i < maxId; i++) {
            for (int j = 0; j < maxId; j++) {
                matrix[i][j] = (i == j) ? 0 : Double.POSITIVE_INFINITY;
            }
        }
        return matrix;
    }

    public void printAdjacencyList() {
        System.out.println("Graph Adjacency List: " + nodeCount + " nodes, " + edgeCount + " edges.");
    }

    public void buildFromDatabase(CustomDynamicArray locations, CustomDynamicArray roads) {
        for (int i = 0; i < locations.size(); i++) {
            addNode((Location) locations.get(i));
        }
        for (int i = 0; i < roads.size(); i++) {
            addEdge((Road) roads.get(i));
        }
    }
}
