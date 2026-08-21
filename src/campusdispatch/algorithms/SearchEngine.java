package campusdispatch.algorithms;

import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.models.ServiceRequest;

/**
 * SearchEngine provides static methods for searching algorithms
 * used in the UG Campus Dispatch project.
 * Implements linear and binary search with step counting for analysis.
 */
public class SearchEngine {
    
    /**
     * Interface for comparing ServiceRequest objects to a key.
     */
    public interface RequestMatcher {
        int compare(ServiceRequest request, String key);
    }

    /**
     * Performs a sequential scan (linear search) on the array.
     * Time Complexity: O(N) worst case, O(1) best case.
     * Space Complexity: O(1).
     *
     * @param array The custom dynamic array to search.
     * @param key The key to search for.
     * @param matcher The matcher to compare requests with the key.
     * @param stats Array to hold step counters (index 0 will be updated with comparison count).
     * @return The index of the found element, or -1 if not found.
     */
    public static int linearSearch(CustomDynamicArray array, String key, RequestMatcher matcher, int[] stats) {
        int comparisons = 0;
        for (int i = 0; i < array.size(); i++) {
            comparisons++;
            ServiceRequest req = (ServiceRequest) array.get(i);
            if (matcher.compare(req, key) == 0) {
                if (stats != null && stats.length > 0) stats[0] = comparisons;
                return i;
            }
        }
        if (stats != null && stats.length > 0) stats[0] = comparisons;
        return -1;
    }

    /**
     * Performs binary search on a SORTED array.
     * PRECONDITION: The input CustomDynamicArray must be sorted according to the same
     * criteria used by the RequestMatcher, otherwise the behavior is undefined.
     * Time Complexity: O(log N).
     * Space Complexity: O(1).
     *
     * @param array The sorted custom dynamic array to search.
     * @param key The key to search for.
     * @param matcher The matcher to compare requests with the key.
     * @param stats Array to hold step counters (index 0 will be updated with comparison count).
     * @return The index of the found element, or -1 if not found.
     */
    public static int binarySearch(CustomDynamicArray array, String key, RequestMatcher matcher, int[] stats) {
        int comparisons = 0;
        int left = 0;
        int right = array.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;
            ServiceRequest req = (ServiceRequest) array.get(mid);
            int cmp = matcher.compare(req, key);

            if (cmp == 0) {
                if (stats != null && stats.length > 0) stats[0] = comparisons;
                return mid;
            } else if (cmp < 0) { // req is less than key, search right half
                left = mid + 1;
            } else { // req is greater than key, search left half
                right = mid - 1;
            }
        }
        if (stats != null && stats.length > 0) stats[0] = comparisons;
        return -1;
    }
}
