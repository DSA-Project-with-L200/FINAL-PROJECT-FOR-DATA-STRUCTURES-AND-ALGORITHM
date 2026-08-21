package campusdispatch.algorithms;

import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.models.ServiceRequest;

/**
 * DynamicProgramming provides DP algorithms for dispatch optimization.
 */
public class DynamicProgramming {

    /**
     * Solves the 0/1 Knapsack problem for selecting which requests to serve
     * under a given capacity (e.g., fuel, time limit, or driver count budget).
     * 
     * @param requests CustomDynamicArray of available requests.
     * @param capacity Maximum allowable capacity.
     * @return A 2D array representing the DP memoization table.
     */
    public static int[][] knapsackAllocation(CustomDynamicArray requests, int capacity) {
        int n = requests.size();
        int[][] dp = new int[n + 1][capacity + 1];
        
        for (int i = 1; i <= n; i++) {
            ServiceRequest req = (ServiceRequest) requests.get(i - 1);
            int cost = req.getExpectedTime(); // Using expected time as cost
            int value = req.getPriority(); // Using priority as value
            
            for (int w = 0; w <= capacity; w++) {
                if (cost <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - cost] + value);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        System.out.println("Knapsack DP Table computed.");
        return dp;
    }

    /**
     * Traces back the DP table to find the selected items.
     * 
     * @param dp The computed DP table.
     * @param requests The original array of requests.
     * @param capacity The maximum capacity used.
     * @return A CustomDynamicArray of the selected ServiceRequest objects.
     */
    public static CustomDynamicArray reconstructSolution(int[][] dp, CustomDynamicArray requests, int capacity) {
        int n = requests.size();
        CustomDynamicArray selected = new CustomDynamicArray(n);
        
        int res = dp[n][capacity];
        int w = capacity;
        
        for (int i = n; i > 0 && res > 0; i--) {
            if (res != dp[i - 1][w]) {
                // Item i-1 was included
                ServiceRequest req = (ServiceRequest) requests.get(i - 1);
                selected.add(req);
                res -= req.getPriority();
                w -= req.getExpectedTime();
            }
        }
        
        return selected;
    }
}
