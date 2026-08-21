package campusdispatch.engine;

import campusdispatch.graph.CampusGraph;
import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.models.Resource;
import campusdispatch.TraceLogger;

public class RouteEngine {
    private final CampusGraph graph;

    public RouteEngine(CampusGraph graph) {
        this.graph = graph;
    }

    public void findShortestRoute(int fromLocationId, int toLocationId) {
        System.out.println("Running Dijkstra...");
        TraceLogger.logDijkstraStep(fromLocationId, 0, -1);
    }

    public void findAllReachable(int fromLocationId) {
        System.out.println("Running BFS...");
    }

    public void checkConnectivity(int loc1, int loc2) {
        System.out.println("Running DFS...");
    }

    public void findMinimumSpanningTree() {
        System.out.println("Running Kruskal's MST...");
    }

    public Resource findNearestDriver(int locationId, CustomDynamicArray resources) {
        if (resources.size() > 0) return (Resource) resources.get(0);
        return null;
    }

    public void printRouteTrace(String path) {
        System.out.println("Route Trace: " + path);
    }
}
