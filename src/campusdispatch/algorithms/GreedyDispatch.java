package campusdispatch.algorithms;

import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.models.ServiceRequest;
import campusdispatch.models.Resource;

/**
 * GreedyDispatch implements a greedy algorithm for assigning drivers to requests.
 */
public class GreedyDispatch {

    /**
     * Assigns the nearest available driver to the highest priority request greedily.
     * 
     * @param requests CustomDynamicArray of ServiceRequest (assumed sorted by priority descending).
     * @param drivers CustomDynamicArray of Resource (available drivers).
     */
    public static void greedyAssign(CustomDynamicArray requests, CustomDynamicArray drivers) {
        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest req = (ServiceRequest) requests.get(i);
            if (req.isAssigned()) continue;
            
            Resource bestDriver = null;
            int minDistance = Integer.MAX_VALUE;
            
            for (int j = 0; j < drivers.size(); j++) {
                Resource drv = (Resource) drivers.get(j);
                if (drv.isAvailable()) {
                    int dist = Math.abs(drv.getLocation() - req.getPickupLocation()); // Simplistic distance
                    if (dist < minDistance) {
                        minDistance = dist;
                        bestDriver = drv;
                    }
                }
            }
            
            if (bestDriver != null) {
                req.setAssigned(true);
                bestDriver.setAvailable(false);
                System.out.println("Assigned Driver " + bestDriver.getId() + " to Request " + req.getId());
            }
        }
    }

    /**
     * Demonstrates where greedy produces suboptimal results compared to DP.
     * Example: We have Driver A at loc 10, Driver B at loc 20.
     * Request 1 (High priority) at loc 12. Request 2 (High priority) at loc 21.
     * Greedy will give Driver A to Request 1 (dist 2). Then Driver B to Request 2 (dist 1). Total dist = 3.
     * Wait, a better counterexample:
     * Driver A at 10, Driver B at 15.
     * Req 1 at 14 (priority 100), Req 2 at 9 (priority 90).
     * Greedy assigns Driver B (dist 1) to Req 1. Driver A (dist 1) to Req 2. Total dist = 2.
     * Actually, if we just care about priority vs distance trade-offs.
     * 
     * @return String detailing the counterexample.
     */
    public static String generateCounterexampleReport() {
        return "COUNTEREXAMPLE TO GREEDY OPTIMALITY:\n" +
               "Scenario: We have limited budget/capacity for assigning trips.\n" +
               "Request A: Cost 5, Value 100 (Ratio 20)\n" +
               "Request B: Cost 4, Value 70 (Ratio 17.5)\n" +
               "Request C: Cost 3, Value 60 (Ratio 20)\n" +
               "Capacity = 7\n" +
               "Greedy approach (by value or ratio) picks A (Cost 5, Value 100). Remaining capacity 2. Cannot pick B or C.\n" +
               "Total Value = 100.\n" +
               "Optimal DP approach: Picks B (Cost 4, Value 70) and C (Cost 3, Value 60).\n" +
               "Total Capacity used = 7, Total Value = 130.\n" +
               "This clearly demonstrates that the 0/1 Knapsack problem structure " +
               "cannot be optimally solved using a greedy approach, and requires Dynamic Programming.";
    }
}
