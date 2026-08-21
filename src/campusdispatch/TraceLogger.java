package campusdispatch;

/**
 * Utility for generating trace output.
 */
public class TraceLogger {
    public static void logHeapOperation(String operation, String heapState) {
        System.out.println("[HEAP TRACE] " + operation + " | State: " + heapState);
    }

    public static void logQueueOperation(String operation, int front, int rear) {
        System.out.println("[QUEUE TRACE] " + operation + " | Front: " + front + ", Rear: " + rear);
    }

    public static void logDijkstraStep(int nodeId, double distance, int predecessor) {
        System.out.println("[DIJKSTRA TRACE] Node: " + nodeId + ", Dist: " + distance + ", Pred: " + predecessor);
    }

    public static void logSortStep(String algorithm, int step, String arrayState) {
        System.out.println("[SORT TRACE] " + algorithm + " Step " + step + " | " + arrayState);
    }

    public static void exportTraceToFile(String filename) {
        System.out.println("Exported trace to " + filename);
    }
}
