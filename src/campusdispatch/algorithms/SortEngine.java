package campusdispatch.algorithms;

import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.models.ServiceRequest;

/**
 * SortEngine provides various sorting algorithms for ServiceRequest objects.
 * Algorithms include Selection Sort, Insertion Sort, Merge Sort, and Quick Sort.
 * Includes tracking for comparisons and swaps.
 */
public class SortEngine {

    /**
     * Interface for comparing two ServiceRequest objects.
     */
    public interface RequestComparator {
        int compare(ServiceRequest a, ServiceRequest b);
    }

    /**
     * Selection sort algorithm (in-place).
     * Time Complexity: O(N^2) in all cases.
     * Space Complexity: O(1).
     * Not stable.
     *
     * @param array The array to sort.
     * @param comparator The comparator determining sort order.
     * @param stats Array for stats: stats[0]=comparisons, stats[1]=swaps.
     */
    public static void selectionSort(CustomDynamicArray array, RequestComparator comparator, long[] stats) {
        long comparisons = 0;
        long swaps = 0;
        int n = array.size();
        
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                ServiceRequest reqJ = (ServiceRequest) array.get(j);
                ServiceRequest reqMin = (ServiceRequest) array.get(minIdx);
                if (comparator.compare(reqJ, reqMin) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                swap(array, i, minIdx);
                swaps++;
            }
        }
        
        if (stats != null && stats.length >= 2) {
            stats[0] = comparisons;
            stats[1] = swaps;
        }
    }

    /**
     * Insertion sort algorithm (in-place).
     * Time Complexity: O(N^2) worst/average, O(N) best.
     * Space Complexity: O(1).
     * Stable sort. Good for small or mostly sorted arrays.
     */
    public static void insertionSort(CustomDynamicArray array, RequestComparator comparator, long[] stats) {
        long comparisons = 0;
        long swaps = 0; // conceptually shifts, we'll count as swaps
        int n = array.size();
        
        for (int i = 1; i < n; i++) {
            ServiceRequest key = (ServiceRequest) array.get(i);
            int j = i - 1;
            
            while (j >= 0) {
                comparisons++;
                ServiceRequest reqJ = (ServiceRequest) array.get(j);
                if (comparator.compare(reqJ, key) > 0) {
                    array.set(j + 1, reqJ); // shift right
                    swaps++;
                    j--;
                } else {
                    break;
                }
            }
            array.set(j + 1, key);
        }
        
        if (stats != null && stats.length >= 2) {
            stats[0] = comparisons;
            stats[1] = swaps;
        }
    }

    /**
     * Merge sort (divide and conquer).
     * Time Complexity: O(N log N) in all cases. Recurrence: T(n) = 2T(n/2) + O(n).
     * Space Complexity: O(N) auxiliary space.
     * Stable sort.
     */
    public static void mergeSort(CustomDynamicArray array, RequestComparator comparator, long[] stats) {
        long[] localStats = new long[2]; // 0: comps, 1: ops (copies)
        mergeSortHelper(array, 0, array.size() - 1, comparator, localStats);
        if (stats != null && stats.length >= 2) {
            stats[0] = localStats[0];
            stats[1] = localStats[1];
        }
    }

    private static void mergeSortHelper(CustomDynamicArray array, int left, int right, RequestComparator comparator, long[] stats) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(array, left, mid, comparator, stats);
            mergeSortHelper(array, mid + 1, right, comparator, stats);
            merge(array, left, mid, right, comparator, stats);
        }
    }

    private static void merge(CustomDynamicArray array, int left, int mid, int right, RequestComparator comparator, long[] stats) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        CustomDynamicArray L = new CustomDynamicArray(n1);
        CustomDynamicArray R = new CustomDynamicArray(n2);
        
        for (int i = 0; i < n1; i++) {
            L.add(array.get(left + i));
            stats[1]++;
        }
        for (int j = 0; j < n2; j++) {
            R.add(array.get(mid + 1 + j));
            stats[1]++;
        }
        
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            stats[0]++; // comparison
            ServiceRequest leftReq = (ServiceRequest) L.get(i);
            ServiceRequest rightReq = (ServiceRequest) R.get(j);
            
            if (comparator.compare(leftReq, rightReq) <= 0) {
                array.set(k, leftReq);
                i++;
            } else {
                array.set(k, rightReq);
                j++;
            }
            stats[1]++; // copy op
            k++;
        }
        
        while (i < n1) {
            array.set(k, L.get(i));
            i++;
            k++;
            stats[1]++;
        }
        
        while (j < n2) {
            array.set(k, R.get(j));
            j++;
            k++;
            stats[1]++;
        }
    }

    /**
     * Quick sort (divide and conquer).
     * Time Complexity: O(N log N) average, O(N^2) worst case.
     * Space Complexity: O(log N) stack space.
     * Not stable.
     */
    public static void quickSort(CustomDynamicArray array, RequestComparator comparator, long[] stats) {
        long[] localStats = new long[2]; // 0: pivots/comps, 1: swaps
        quickSortHelper(array, 0, array.size() - 1, comparator, localStats);
        if (stats != null && stats.length >= 2) {
            stats[0] = localStats[0];
            stats[1] = localStats[1];
        }
    }

    private static void quickSortHelper(CustomDynamicArray array, int low, int high, RequestComparator comparator, long[] stats) {
        if (low < high) {
            int pi = partition(array, low, high, comparator, stats);
            quickSortHelper(array, low, pi - 1, comparator, stats);
            quickSortHelper(array, pi + 1, high, comparator, stats);
        }
    }

    private static int partition(CustomDynamicArray array, int low, int high, RequestComparator comparator, long[] stats) {
        ServiceRequest pivot = (ServiceRequest) array.get(high);
        int i = (low - 1);
        
        for (int j = low; j < high; j++) {
            stats[0]++; // comparison
            ServiceRequest curr = (ServiceRequest) array.get(j);
            if (comparator.compare(curr, pivot) < 0) {
                i++;
                swap(array, i, j);
                stats[1]++;
            }
        }
        swap(array, i + 1, high);
        stats[1]++;
        return i + 1;
    }

    private static void swap(CustomDynamicArray array, int i, int j) {
        Object temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }
}
