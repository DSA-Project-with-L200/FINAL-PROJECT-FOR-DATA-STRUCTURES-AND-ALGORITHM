package campusdispatch.datastructures;

/**
 * Custom Graph data structure representing an adjacency list graph.
 * Compatible with GraphAlgorithms.
 */
public class Graph {
    private int numVertices;
    private CustomDynamicArray<CustomDynamicArray<Edge>> adjList;

    public static class Edge {
        public int source;
        public int destination;
        public int weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static class NodeDistance implements Comparable<NodeDistance> {
        public int nodeId;
        public int distance;

        public NodeDistance(int nodeId, int distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public static class MSTEdge implements Comparable<MSTEdge> {
        public int source;
        public int destination;
        public int weight;

        public MSTEdge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public int compareTo(MSTEdge other) {
            return Integer.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return "Edge(" + source + " -> " + destination + ", weight=" + weight + ")";
        }
    }

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new CustomDynamicArray<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adjList.add(new CustomDynamicArray<Edge>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        if (source >= 0 && source < numVertices && destination >= 0 && destination < numVertices) {
            adjList.get(source).add(new Edge(source, destination, weight));
        }
    }

    public int getNumVertices() {
        return numVertices;
    }

    public CustomDynamicArray<Edge> getNeighbors(int node) {
        if (node >= 0 && node < numVertices) {
            return adjList.get(node);
        }
        return new CustomDynamicArray<>();
    }

    public CustomDynamicArray<MSTEdge> getAllEdges() {
        CustomDynamicArray<MSTEdge> edges = new CustomDynamicArray<>();
        for (int i = 0; i < numVertices; i++) {
            CustomDynamicArray<Edge> neighbors = adjList.get(i);
            for (int j = 0; j < neighbors.size(); j++) {
                Edge e = neighbors.get(j);
                edges.add(new MSTEdge(e.source, e.destination, e.weight));
            }
        }
        return edges;
    }
}
