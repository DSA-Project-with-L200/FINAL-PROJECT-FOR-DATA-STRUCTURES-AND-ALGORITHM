package campusdispatch.algorithms;

import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.datastructures.CustomQueue;
import campusdispatch.datastructures.CustomStack;
import campusdispatch.datastructures.CustomMinHeap;
import campusdispatch.datastructures.CustomDisjointSet;
import campusdispatch.datastructures.Graph;
import campusdispatch.datastructures.Graph.Edge;

/**
 * GraphAlgorithms implements traversals and pathfinding for the campus map graph.
 * Uses only custom data structures.
 */
public class GraphAlgorithms {

    /**
     * Breadth-First Search traversal.
     * Uses CustomQueue to visit nodes level by level.
     * 
     * @param graph The campus graph.
     * @param startNode The starting node ID.
     * @return A CustomDynamicArray containing the traversal order of node IDs.
     */
    public static CustomDynamicArray bfs(Graph graph, int startNode) {
        System.out.println("Starting BFS from node: " + startNode);
        CustomDynamicArray traversal = new CustomDynamicArray(graph.getNumVertices());
        boolean[] visited = new boolean[graph.getNumVertices()];
        CustomQueue queue = new CustomQueue();

        visited[startNode] = true;
        queue.enqueue(startNode);

        while (!queue.isEmpty()) {
            int curr = (int) queue.dequeue();
            traversal.add(curr);
            System.out.println("BFS visited: " + curr);

            CustomDynamicArray neighbors = graph.getNeighbors(curr);
            for (int i = 0; i < neighbors.size(); i++) {
                Edge edge = (Edge) neighbors.get(i);
                if (!visited[edge.destination]) {
                    visited[edge.destination] = true;
                    queue.enqueue(edge.destination);
                }
            }
        }
        return traversal;
    }

    /**
     * Depth-First Search traversal (Iterative).
     * Uses CustomStack.
     * 
     * @param graph The campus graph.
     * @param startNode The starting node ID.
     * @return A CustomDynamicArray containing the traversal order.
     */
    public static CustomDynamicArray dfs(Graph graph, int startNode) {
        System.out.println("Starting DFS from node: " + startNode);
        CustomDynamicArray traversal = new CustomDynamicArray(graph.getNumVertices());
        boolean[] visited = new boolean[graph.getNumVertices()];
        CustomStack stack = new CustomStack();

        stack.push(startNode);

        while (!stack.isEmpty()) {
            int curr = (int) stack.pop();

            if (!visited[curr]) {
                visited[curr] = true;
                traversal.add(curr);
                System.out.println("DFS visited: " + curr);

                CustomDynamicArray neighbors = graph.getNeighbors(curr);
                // Push in reverse order to visit in forward order
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    Edge edge = (Edge) neighbors.get(i);
                    if (!visited[edge.destination]) {
                        stack.push(edge.destination);
                    }
                }
            }
        }
        return traversal;
    }

    /**
     * Dijkstra's Algorithm for finding shortest paths.
     * Uses CustomMinHeap.
     * 
     * @param graph The campus graph.
     * @param source The starting node ID.
     * @param destination The target node ID.
     * @return A CustomDynamicArray representing the path, or empty if no path.
     */
    public static CustomDynamicArray dijkstra(Graph graph, int source, int destination) {
        System.out.println("Starting Dijkstra from " + source + " to " + destination);
        int n = graph.getNumVertices();
        int[] dist = new int[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        CustomMinHeap heap = new CustomMinHeap(n * 2);
        dist[source] = 0;
        heap.insert(new Graph.NodeDistance(source, 0));

        while (!heap.isEmpty()) {
            Graph.NodeDistance curr = (Graph.NodeDistance) heap.extractMin();
            int u = curr.nodeId;

            if (curr.distance > dist[u]) continue;
            
            System.out.println("Dijkstra expanding node " + u + " with dist " + curr.distance);

            if (u == destination) break; // Found shortest path

            CustomDynamicArray neighbors = graph.getNeighbors(u);
            for (int i = 0; i < neighbors.size(); i++) {
                Edge edge = (Edge) neighbors.get(i);
                int v = edge.destination;
                int weight = edge.weight;

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    heap.insert(new Graph.NodeDistance(v, dist[v]));
                    System.out.println("  -> Updated dist for node " + v + " to " + dist[v]);
                }
            }
        }

        CustomDynamicArray path = new CustomDynamicArray(n);
        if (dist[destination] == Integer.MAX_VALUE) {
            System.out.println("No path found.");
            return path;
        }

        // Reconstruct path
        int curr = destination;
        CustomStack reversePath = new CustomStack();
        while (curr != -1) {
            reversePath.push(curr);
            curr = parent[curr];
        }
        while (!reversePath.isEmpty()) {
            path.add(reversePath.pop());
        }
        return path;
    }

    /**
     * Prim's Minimum Spanning Tree Algorithm.
     * 
     * @param graph The campus graph.
     * @return CustomDynamicArray of edges in the MST.
     */
    public static CustomDynamicArray primMST(Graph graph) {
        System.out.println("Starting Prim's MST");
        int n = graph.getNumVertices();
        boolean[] inMST = new boolean[n];
        CustomDynamicArray mstEdges = new CustomDynamicArray(n);
        CustomMinHeap heap = new CustomMinHeap(n * n);

        int startNode = 0; // Start arbitrarily at 0
        inMST[startNode] = true;
        
        // Initial edges from start node
        CustomDynamicArray startNeighbors = graph.getNeighbors(startNode);
        for(int i = 0; i < startNeighbors.size(); i++) {
            Edge e = (Edge) startNeighbors.get(i);
            heap.insert(new Graph.MSTEdge(startNode, e.destination, e.weight));
        }

        int totalCost = 0;
        int edgesFound = 0;

        while (!heap.isEmpty() && edgesFound < n - 1) {
            Graph.MSTEdge minEdge = (Graph.MSTEdge) heap.extractMin();
            
            if (inMST[minEdge.destination]) continue;

            inMST[minEdge.destination] = true;
            mstEdges.add(minEdge);
            totalCost += minEdge.weight;
            edgesFound++;
            System.out.println("Prim added edge: " + minEdge.source + " - " + minEdge.destination + " cost: " + minEdge.weight);

            CustomDynamicArray neighbors = graph.getNeighbors(minEdge.destination);
            for (int i = 0; i < neighbors.size(); i++) {
                Edge e = (Edge) neighbors.get(i);
                if (!inMST[e.destination]) {
                    heap.insert(new Graph.MSTEdge(minEdge.destination, e.destination, e.weight));
                }
            }
        }
        System.out.println("Total MST Cost: " + totalCost);
        return mstEdges;
    }

    /**
     * Kruskal's Minimum Spanning Tree Algorithm.
     * Uses CustomDisjointSet.
     * 
     * @param graph The campus graph.
     * @return CustomDynamicArray of edges in the MST.
     */
    public static CustomDynamicArray kruskalMST(Graph graph) {
        System.out.println("Starting Kruskal's MST");
        int n = graph.getNumVertices();
        CustomDynamicArray allEdges = graph.getAllEdges();
        
        // Sort all edges based on weight (using insertion sort as they are simple objects)
        for (int i = 1; i < allEdges.size(); i++) {
            Graph.MSTEdge key = (Graph.MSTEdge) allEdges.get(i);
            int j = i - 1;
            while (j >= 0 && ((Graph.MSTEdge)allEdges.get(j)).weight > key.weight) {
                allEdges.set(j + 1, allEdges.get(j));
                j--;
            }
            allEdges.set(j + 1, key);
        }

        CustomDisjointSet dsu = new CustomDisjointSet(n);
        CustomDynamicArray mstEdges = new CustomDynamicArray(n);
        int totalCost = 0;

        for (int i = 0; i < allEdges.size(); i++) {
            Graph.MSTEdge edge = (Graph.MSTEdge) allEdges.get(i);
            if (dsu.find(edge.source) != dsu.find(edge.destination)) {
                dsu.union(edge.source, edge.destination);
                mstEdges.add(edge);
                totalCost += edge.weight;
                System.out.println("Kruskal added edge: " + edge.source + " - " + edge.destination + " cost: " + edge.weight);
            }
        }
        
        System.out.println("Total MST Cost: " + totalCost);
        return mstEdges;
    }
}
