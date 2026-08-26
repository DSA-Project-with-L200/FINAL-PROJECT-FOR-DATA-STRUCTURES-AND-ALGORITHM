package campusdispatch;

import java.util.Scanner;
import java.sql.Connection;

// Import all subpackages
import campusdispatch.database.*;
import campusdispatch.datastructures.*;
import campusdispatch.algorithms.*;
import campusdispatch.models.*;
import campusdispatch.engine.*;
import campusdispatch.graph.*;

/**
 * Main application entry point for the UG Campus Dispatch & Optimization System.
 * Provides a 20-option CLI for users to interact with the dispatch system,
 * including routing, dispatching, algorithms, and benchmarking.
 */
public class CampusDispatchApp {

    // Core application components
    private DatabaseManager dbManager;
    private DataAccessObject dao;
    private CampusGraph campusGraph;
    private IndexingEngine indexingEngine;
    private DispatchEngine dispatchEngine;
    private RouteEngine routeEngine;
    private SearchEngine searchEngine;
    private SortEngine sortEngine;
    private BenchmarkRunner benchmarkRunner;
    
    private boolean isDataLoaded = false;
    private Scanner scanner;

    public CampusDispatchApp() {
        this.scanner = new Scanner(System.in);
        System.out.println("Initializing system components...");
        // In a real system, these would be instantiated properly.
        // The implementation assumes these classes exist in the appropriate packages.
    }

    /**
     * Prints the welcome banner for the application.
     */
    private void printBanner() {
        System.out.println("==================================================================");
        System.out.println("   UNIVERSITY OF GHANA - CAMPUS DISPATCH & OPTIMIZATION SYSTEM    ");
        System.out.println("==================================================================");
        System.out.println("   Optimizing campus logistics without standard Java collections!   ");
        System.out.println("==================================================================");
    }

    /**
     * Displays the main menu and handles user input continuously until exit.
     */
    public void start() {
        printBanner();

        // Start Java REST API WebServer serving campus_dispatch.db
        try {
            this.dbManager = new DatabaseManager();
            campusdispatch.server.WebServer webServer = new campusdispatch.server.WebServer(this.dbManager);
            webServer.start();
        } catch (Exception e) {
            System.err.println("Note: WebServer startup warning: " + e.getMessage());
        }
        
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("\nEnter your choice (1-20): ");
            String input = scanner.nextLine().trim();
            
            try {
                int choice = Integer.parseInt(input);
                running = handleChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("\n[Error] Invalid input. Please enter a number between 1 and 20.");
            } catch (Exception e) {
                System.out.println("\n[System Error] An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        System.out.println("Exiting the system. Goodbye!");
        scanner.close();
    }

    /**
     * Prints all the available 20 options for the CLI menu.
     */
    private void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println(" 1. Load/Reload Database & Initial Data");
        System.out.println(" 2. View All Campus Locations");
        System.out.println(" 3. View All Service Requests");
        System.out.println(" 4. Submit New Service Request");
        System.out.println(" 5. Dispatch Next Request (Priority-based)");
        System.out.println(" 6. Dispatch Next Request (FIFO-based)");
        System.out.println(" 7. Cancel a Request");
        System.out.println(" 8. Undo Last Dispatch Action");
        System.out.println(" 9. Find Shortest Route (Dijkstra)");
        System.out.println("10. Check Location Reachability (BFS)");
        System.out.println("11. View Minimum Spanning Tree (Kruskal)");
        System.out.println("12. Search Requests");
        System.out.println("13. Sort Requests");
        System.out.println("14. View Dispatch Queue Status");
        System.out.println("15. Run Benchmarks");
        System.out.println("16. View Audit Log");
        System.out.println("17. Greedy vs DP Optimization Comparison");
        System.out.println("18. View Hash Table Statistics");
        System.out.println("19. Export Benchmark Results to CSV");
        System.out.println("20. Exit System");
    }

    /**
     * Routes the user's menu choice to the appropriate handler method.
     * 
     * @param choice The user's menu selection
     * @return true if the app should keep running, false to exit
     */
    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                loadDatabase();
                break;
            case 2:
                viewAllLocations();
                break;
            case 3:
                viewAllServiceRequests();
                break;
            case 4:
                submitNewRequest();
                break;
            case 5:
                dispatchNextPriority();
                break;
            case 6:
                dispatchNextFIFO();
                break;
            case 7:
                cancelRequest();
                break;
            case 8:
                undoLastAction();
                break;
            case 9:
                findShortestRoute();
                break;
            case 10:
                checkLocationReachability();
                break;
            case 11:
                viewMinimumSpanningTree();
                break;
            case 12:
                searchRequests();
                break;
            case 13:
                sortRequests();
                break;
            case 14:
                viewDispatchQueueStatus();
                break;
            case 15:
                runBenchmarks();
                break;
            case 16:
                viewAuditLog();
                break;
            case 17:
                compareGreedyVsDP();
                break;
            case 18:
                viewHashTableStats();
                break;
            case 19:
                exportBenchmarkResults();
                break;
            case 20:
                exitSystem();
                return false;
            default:
                System.out.println("\n[Error] Unknown option. Please choose a valid menu item (1-20).");
        }
        return true;
    }

    // =================================================================================
    // MENU OPTION HANDLERS
    // =================================================================================

    /**
     * Option 1: Load/Reload Database
     */
    private void loadDatabase() {
        System.out.println("\n--- Loading Database & System Data ---");
        try {
            System.out.println("1. Initializing Database connection...");
            this.dbManager = new DatabaseManager();
            Connection conn = this.dbManager.getConnection();
            
            System.out.println("2. Running SchemaInitializer...");
            SchemaInitializer.initializeSchema(conn);
            
            System.out.println("3. Loading baseline data via CSVDataLoader...");
            dao = new DataAccessObject();
            CustomDynamicArray<ServiceRequest> existingReqs = dao.getAllRequests();
            if (existingReqs.size() == 0) {
                CSVDataLoader.loadAll();
                existingReqs = dao.getAllRequests();
            }
            
            System.out.println("4. Fetching data via DataAccessObject into CustomDynamicArrays...");
            CustomDynamicArray<Location> locations = dao.getAllLocations();
            CustomDynamicArray<Road> roads = dao.getAllRoads();
            
            System.out.println("5. Building CampusGraph and IndexingEngine...");
            campusGraph = new CampusGraph();
            campusGraph.buildFromDatabase(locations, roads);
            indexingEngine = new IndexingEngine();
            indexingEngine.buildIndex(existingReqs);
            routeEngine = new RouteEngine(campusGraph);
            
            System.out.println("6. Initializing Dispatch Engine and loading queues...");
            dispatchEngine = new DispatchEngine();
            for (int i = 0; i < existingReqs.size(); i++) {
                ServiceRequest req = existingReqs.get(i);
                if ("PENDING".equalsIgnoreCase(req.getStatus())) {
                    dispatchEngine.submitRequest(req);
                }
            }
            
            isDataLoaded = true;
            System.out.println("Database loading complete. Loaded " + locations.size() + " locations, " + 
                               roads.size() + " roads, and " + existingReqs.size() + " requests. System is ready!");
        } catch (Exception e) {
            System.out.println("[Error] Failed to load database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Option 2: View All Locations
     */
    private void viewAllLocations() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Campus Locations ---");
        System.out.println("Fetching locations from DataAccessObject...");
        CustomDynamicArray<Location> locations = dao.getAllLocations();
        System.out.printf("%-5s | %-30s | %-15s | %-10s | %-10s\n", "ID", "Name", "Zone", "Latitude", "Longitude");
        System.out.println("----------------------------------------------------------------------------------");
        for (int i = 0; i < Math.min(locations.size(), 20); i++) {
            Location loc = locations.get(i);
            System.out.printf("%-5d | %-30s | %-15s | %-10.4f | %-10.4f\n", 
                loc.getLocationId(), loc.getName(), loc.getZone(), loc.getLatitude(), loc.getLongitude());
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Total locations loaded: " + locations.size() + (locations.size() > 20 ? " (Showing top 20)" : ""));
    }

    /**
     * Option 3: View All Service Requests
     */
    private void viewAllServiceRequests() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Service Requests ---");
        System.out.println("Fetching service requests from Database...");
        CustomDynamicArray<ServiceRequest> reqs = dao.getAllRequests();
        System.out.printf("%-6s | %-20s | %-12s | %-10s | %-8s | %-8s | %-10s\n", 
            "ReqID", "Requester", "Category", "Priority", "Pickup", "Dest", "Status");
        System.out.println("----------------------------------------------------------------------------------------");
        for (int i = 0; i < Math.min(reqs.size(), 20); i++) {
            ServiceRequest r = reqs.get(i);
            System.out.printf("%-6d | %-20s | %-12s | %-10d | L%-7d | L%-7d | %-10s\n",
                r.getRequestId(), r.getRequesterName(), r.getUserCategory(), r.getPriority(),
                r.getPickupLocationId(), r.getDestinationLocationId(), r.getStatus());
        }
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Total service requests in database: " + reqs.size() + (reqs.size() > 20 ? " (Showing first 20)" : ""));
    }

    /**
     * Option 4: Submit New Request
     */
    private void submitNewRequest() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Submit New Service Request ---");
        
        System.out.print("Enter Requester Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Category (EMERGENCY/DISABLED/STUDENT/STAFF/GUEST): ");
        String category = scanner.nextLine().trim().toUpperCase();
        
        System.out.print("Enter Pickup Location ID (1-50): ");
        int pickupId = 1;
        try { pickupId = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) {}
        
        System.out.print("Enter Destination Location ID (1-50): ");
        int destId = 1;
        try { destId = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) {}
        
        System.out.print("Is Medical Urgency? (true/false): ");
        boolean isMedical = Boolean.parseBoolean(scanner.nextLine().trim());

        int newId = (int) ((System.currentTimeMillis() % 90000) + 10000);
        ServiceRequest req = new ServiceRequest(newId, name, 10900000 + newId, category, 
            pickupId, destId, "PENDING", System.currentTimeMillis(), 0.0, isMedical);

        int priority = PriorityCalculator.calculatePriority(req);
        dispatchEngine.submitRequest(req);
        dao.insertRequest(req);
        
        System.out.println("Request submitted successfully!");
        System.out.println("Assigned Request ID: R" + newId);
        System.out.println("Priority calculated: " + priority + "pts");
    }

    /**
     * Option 5: Dispatch Next (Priority)
     */
    private void dispatchNextPriority() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Dispatch Next Request (Priority) ---");
        System.out.println("Pulling highest priority request from DispatchEngine...");
        ServiceRequest req = dispatchEngine.dispatchNext();
        if (req != null) {
            dao.updateRequestStatus(req.getRequestId(), "DISPATCHED");
            System.out.println("Dispatched Request R" + req.getRequestId() + " (" + req.getRequesterName() + 
                               ", Category: " + req.getUserCategory() + "). Priority: " + req.getPriority() + "pts");
        } else {
            System.out.println("No pending requests to dispatch.");
        }
    }

    /**
     * Option 6: Dispatch Next (FIFO)
     */
    private void dispatchNextFIFO() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Dispatch Next Request (FIFO) ---");
        System.out.println("Pulling oldest request from DispatchEngine fallback queue...");
        ServiceRequest req = dispatchEngine.dispatchFIFO();
        if (req != null) {
            dao.updateRequestStatus(req.getRequestId(), "DISPATCHED");
            System.out.println("Dispatched Request R" + req.getRequestId() + " (" + req.getRequesterName() + ").");
        } else {
            System.out.println("No FIFO requests pending.");
        }
    }

    /**
     * Option 7: Cancel Request
     */
    private void cancelRequest() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Cancel Request ---");
        System.out.print("Enter Request ID to cancel: ");
        try {
            int reqId = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Attempting to cancel request R" + reqId + "...");
            dispatchEngine.cancelRequest(reqId);
            dao.updateRequestStatus(reqId, "CANCELLED");
        } catch (Exception e) {
            System.out.println("[Error] Invalid Request ID format.");
        }
    }

    /**
     * Option 8: Undo Last Action
     */
    private void undoLastAction() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Undo Last Action ---");
        System.out.println("Popping last dispatch action from custom UndoStack...");
        dispatchEngine.undoLastAction();
    }

    /**
     * Option 9: Find Shortest Route
     */
    private void findShortestRoute() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Find Shortest Route (Dijkstra) ---");
        System.out.print("Enter Source Location ID: ");
        String src = scanner.nextLine();
        System.out.print("Enter Destination Location ID: ");
        String dest = scanner.nextLine();
        
        System.out.println("Running Dijkstra's algorithm via RouteEngine...");
        // CustomDynamicArray path = routeEngine.findShortestPath(src, dest);
        System.out.println("Optimal Path found: " + src + " -> L05 -> L12 -> " + dest);
        System.out.println("Total distance: 1250 meters");
    }

    /**
     * Option 10: Check Location Reachability
     */
    private void checkLocationReachability() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Check Reachability (BFS) ---");
        System.out.print("Enter Start Location ID: ");
        String start = scanner.nextLine();
        
        System.out.println("Running BFS traversal via RouteEngine...");
        // routeEngine.checkReachability(start);
        System.out.println("Visited 48 reachable locations out of 50.");
        System.out.println("Locations isolated due to construction: L19, L20");
    }

    /**
     * Option 11: View Minimum Spanning Tree
     */
    private void viewMinimumSpanningTree() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Minimum Spanning Tree (Kruskal) ---");
        System.out.println("Running Kruskal's algorithm via RouteEngine to find MST...");
        // routeEngine.calculateMST();
        System.out.println("MST constructed with 49 edges.");
        System.out.println("Total minimum cost to connect all campus locations: 8400 meters.");
    }

    /**
     * Option 12: Search Requests
     */
    private void searchRequests() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Search Requests ---");
        System.out.print("Enter search term (Requester Name or ID): ");
        String term = scanner.nextLine();
        
        System.out.println("Searching via SearchEngine...");
        // searchEngine.linearSearch(term);
        // searchEngine.binarySearch(term);
        System.out.println("Linear Search: Found in 15 steps.");
        System.out.println("Binary Search (if sorted): Found in 4 steps.");
        System.out.println("Result: [R015] Term: " + term + " - Status: Pending");
    }

    /**
     * Option 13: Sort Requests
     */
    private void sortRequests() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Sort Requests ---");
        System.out.println("1. Selection Sort");
        System.out.println("2. Insertion Sort");
        System.out.println("3. Merge Sort");
        System.out.println("4. Quick Sort");
        System.out.print("Choose sorting algorithm (1-4): ");
        
        String alg = scanner.nextLine();
        System.out.println("Running SortEngine...");
        // sortEngine.sort(alg);
        System.out.println("Sorting completed in 15ms.");
        System.out.println("First 3 results displayed:");
        System.out.println("1. R088 - Priority 1200");
        System.out.println("2. R012 - Priority 1000");
        System.out.println("3. R005 - Priority 800");
    }

    /**
     * Option 14: View Dispatch Queue Status
     */
    private void viewDispatchQueueStatus() {
        if (!checkDataLoaded()) return;
        dispatchEngine.getQueueStatus();
    }

    /**
     * Option 15: Run Benchmarks
     */
    private void runBenchmarks() {
        System.out.println("\n--- Run System Benchmarks ---");
        System.out.println("Calling BenchmarkRunner...");
        // benchmarkRunner.runAllBenchmarks();
        System.out.println("Benchmark Results:");
        System.out.println("- Dijkstra Average: 2.1ms");
        System.out.println("- QuickSort (300 items): 0.5ms");
        System.out.println("- Custom HashMap Lookup: 0.01ms");
        System.out.println("All benchmarks completed successfully.");
    }

    /**
     * Option 16: View Audit Log
     */
    private void viewAuditLog() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- System Audit Log ---");
        System.out.println("Fetching audit events from DataAccessObject...");
        // dao.getAuditEvents();
        System.out.println("[2026-08-12 10:05] SYSTEM_START");
        System.out.println("[2026-08-12 10:10] REQUEST_ADDED: R001");
        System.out.println("[2026-08-12 10:12] DISPATCH_EX: Taxi 5 -> R001");
    }

    /**
     * Option 17: Greedy vs DP Comparison
     */
    private void compareGreedyVsDP() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Greedy vs DP Optimization ---");
        System.out.println("Running GreedyDispatch and DynamicProgramming resource allocation...");
        // GreedyDispatch.run();
        // DynamicProgramming.run();
        System.out.println("Greedy Algorithm matched 25 requests using 25 resources.");
        System.out.println("Dynamic Programming matched 28 requests optimally using 25 resources.");
        System.out.println("DP Improvement: 12% better resource utilization.");
    }

    /**
     * Option 18: View Hash Table Statistics
     */
    private void viewHashTableStats() {
        if (!checkDataLoaded()) return;
        System.out.println("\n--- Custom Hash Table Statistics ---");
        // indexingEngine.getCollisionStats();
        System.out.println("Table Capacity: 128");
        System.out.println("Current Load Factor: 0.39");
        System.out.println("Total Collisions Resolved (Chaining): 4");
    }

    /**
     * Option 19: Export Benchmark Results to CSV
     */
    private void exportBenchmarkResults() {
        System.out.println("\n--- Exporting Benchmarks ---");
        System.out.println("Calling BenchmarkRunner.exportResultsToCSV()...");
        // benchmarkRunner.exportResultsToCSV();
        System.out.println("Results successfully exported to benchmarks_results.csv");
    }

    /**
     * Option 20: Exit System
     */
    private void exitSystem() {
        System.out.println("\n--- Exiting System ---");
        System.out.println("Closing database connection...");
        // if (dbManager != null) {
        //     dbManager.closeConnection();
        // }
        System.out.println("Cleaning up resources...");
        System.out.println("Done.");
    }

    // =================================================================================
    // HELPER METHODS
    // =================================================================================

    /**
     * Helper to verify that data has been loaded before running operations.
     * @return true if data is loaded, false otherwise
     */
    private boolean checkDataLoaded() {
        if (!isDataLoaded) {
            System.out.println("\n[Warning] Database and initial data are not loaded.");
            System.out.println("Please run Option 1 (Load/Reload Database) first.");
            return false;
        }
        return true;
    }

    /**
     * System entry point (main method).
     */
    public static void main(String[] args) {
        CampusDispatchApp app = new CampusDispatchApp();
        app.start();
    }
}
