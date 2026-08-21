# Algorithms Guide

Complete reference for every algorithm implemented in this project, with time complexities, use cases, and trace evidence requirements.

---

## Searching Algorithms

### Linear Search
**File:** `algorithms/SearchEngine.java`  
**Complexity:** O(n)  
**How it works:** Scans every element from start to end until the target is found or the array is exhausted.

**When to use:** When data is unsorted or when the list is small.

**Trace evidence:** Step counter shows how many comparisons were needed.

---

### Binary Search
**File:** `algorithms/SearchEngine.java`  
**Complexity:** O(log n)  
**How it works:** Repeatedly divides the sorted array in half, comparing the middle element with the target.

**Precondition:** The array MUST be sorted. If unsorted, results are incorrect. This is a required counterexample in the report.

**Trace evidence:** Shows low, mid, high pointers at each step.

---

## Sorting Algorithms

### Selection Sort
**File:** `algorithms/SortEngine.java`  
**Complexity:** O(n²) all cases  
**Stability:** NOT stable  
**In-place:** Yes  
**How it works:** Finds the minimum element in the unsorted portion, swaps it into position.

---

### Insertion Sort
**File:** `algorithms/SortEngine.java`  
**Complexity:** O(n²) worst/average, O(n) best (already sorted)  
**Stability:** Stable  
**In-place:** Yes  
**How it works:** Takes each element and inserts it into its correct position in the sorted portion.

---

### Merge Sort
**File:** `algorithms/SortEngine.java`  
**Complexity:** O(n log n) all cases  
**Stability:** Stable  
**In-place:** No (requires auxiliary arrays)  
**Recurrence:** T(n) = 2T(n/2) + O(n)

**How it works:**
1. Divide the array into two halves
2. Recursively sort each half
3. Merge the two sorted halves

---

### Quick Sort
**File:** `algorithms/SortEngine.java`  
**Complexity:** O(n log n) average, O(n²) worst case  
**Stability:** NOT stable  
**In-place:** Yes (in-place partitioning)  
**Recurrence:** T(n) = T(k) + T(n-k-1) + O(n) where k is pivot position

**How it works:**
1. Choose a pivot element
2. Partition array: elements < pivot go left, elements > pivot go right
3. Recursively sort each partition

---

## Graph Algorithms

### Breadth-First Search (BFS)
**File:** `algorithms/GraphAlgorithms.java`  
**Complexity:** O(V + E)  
**Data structure used:** CustomQueue  

**How it works:**
1. Start at source node, mark as visited, enqueue
2. While queue is not empty: dequeue node, visit all unvisited neighbors, enqueue them
3. Explores level by level (closest nodes first)

**Use case:** Finding all reachable locations from a dispatch point. Answers "Which locations can a taxi reach from here?"

---

### Depth-First Search (DFS)
**File:** `algorithms/GraphAlgorithms.java`  
**Complexity:** O(V + E)  
**Data structure used:** CustomStack (iterative version)  

**How it works:**
1. Start at source node, push to stack
2. While stack is not empty: pop node, if unvisited mark and push all unvisited neighbors
3. Explores as deep as possible before backtracking

**Use case:** Checking if two locations are connected (connectivity check). Answers "Can a taxi get from A to B?"

---

### Dijkstra's Algorithm
**File:** `algorithms/GraphAlgorithms.java`  
**Complexity:** O((V + E) log V) with min-heap  
**Data structure used:** CustomMinHeap  

**How it works:**
1. Set distance to source = 0, all others = infinity
2. Insert source into min-heap
3. While heap is not empty: extract min-distance node, relax all edges (update neighbor distances if shorter path found)
4. Track predecessor for path reconstruction

**Use case:** Finding the shortest route between two campus locations (e.g., Pentagon to UG Hospital).

**Trace evidence required:** Distance table showing updates at each step, plus the final reconstructed path.

---

### Prim's MST
**File:** `algorithms/GraphAlgorithms.java`  
**Complexity:** O((V + E) log V) with min-heap  
**Data structure used:** CustomMinHeap  

**How it works:**
1. Start from any node, add all its edges to a min-heap
2. Extract the minimum-weight edge that connects to an unvisited node
3. Add that node to the MST, add its edges to the heap
4. Repeat until all nodes are in the MST

**Use case:** Finding the minimum-cost road network connecting all campus locations.

---

### Kruskal's MST
**File:** `algorithms/GraphAlgorithms.java`  
**Complexity:** O(E log E) for sorting edges  
**Data structure used:** CustomDisjointSet  

**How it works:**
1. Sort all edges by weight
2. For each edge (lightest first): if the two endpoints are in different sets, add the edge to MST and union the sets
3. Repeat until V-1 edges are added

**Use case:** Same as Prim's — finding minimum spanning tree. Uses Union-Find to efficiently detect cycles.

---

## Optimization Algorithms

### Greedy Dispatch
**File:** `algorithms/GreedyDispatch.java`  

**How it works:** For each request (highest priority first), greedily assign the nearest available driver. This is fast but not always optimal.

**Counterexample:** The code includes a documented scenario where greedy assignment results in higher total wait time than the DP solution. For example:
- Greedy assigns Driver A (close) to Request 1, leaving Driver B (far) for Request 2
- Optimal solution assigns Driver B to Request 1 and Driver A to Request 2, reducing total distance

This counterexample is required for the M8 module deliverable.

---

### Dynamic Programming (0/1 Knapsack)
**File:** `algorithms/DynamicProgramming.java`  
**Complexity:** O(n × W) where n = number of requests, W = capacity  

**How it works:**
1. Build a 2D table where `dp[i][w]` = maximum value achievable using first i items with capacity w
2. For each item: either include it (if it fits) or exclude it, taking the better option
3. Reconstruct solution by tracing back through the table

**Use case:** Optimizing which requests to serve when there's a limited number of taxis or budget. Answers "Given 5 available taxis, which subset of 20 pending requests should we serve to maximize value?"

**Trace evidence:** Print the DP table and the reconstruction steps.

---

## Benchmarking

### BenchmarkRunner
**File:** `algorithms/BenchmarkRunner.java`

Runs all algorithms over varying input sizes and measures execution time in nanoseconds.

**Experiment sizes:** N = {100, 500, 1000, 5000, 10000, 50000}

**What gets benchmarked:**
| Experiment | Algorithms Compared |
|-----------|-------------------|
| Search | Linear search vs Binary search |
| Sorting | Selection, Insertion, Merge, Quick sort |
| Hash Table | Put/Get at load factors 0.1 to 0.9 |
| Heap | Insert/Extract at varying sizes |
| Graph | BFS, DFS, Dijkstra, Prim, Kruskal |

**Output:** CSV file with columns: algorithm, inputSize, avgTimeNs

**How to run:**
1. Select menu option **15** to run benchmarks
2. Select menu option **19** to export results to CSV
3. Plot graphs using Excel, Python, or Java charting libraries
