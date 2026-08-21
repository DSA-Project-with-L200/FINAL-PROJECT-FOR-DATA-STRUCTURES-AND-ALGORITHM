# Module-by-Module Guide

This document maps every project module (M1–M10) to the actual code files, explaining what each module covers and which team members should focus on it.

---

## M1: Input-Output and Problem Specification

**What to deliver:** Computational problem definitions, expected inputs/outputs, assumptions, constraints, and pseudocode or flowcharts for at least five major operations.

**Relevant code:**
- `models/ServiceRequest.java` — defines the main problem entity (dispatch requests)
- `models/Location.java`, `models/Road.java`, `models/Resource.java` — define the problem domain
- `engine/PriorityCalculator.java` — the core priority computation (the main "problem" being solved)

**What to document in your report:**
- Five operations with pseudocode: (1) priority dispatch, (2) FIFO dispatch, (3) shortest route, (4) request search, (5) request sorting
- For each: input format, output format, preconditions, edge cases

---

## M2: Database and Data Loader

**Assigned to:** L200 (DSA I)

**Relevant code:**
- `data/schema.sql` — table definitions
- `data/locations.csv`, `roads.csv`, `service_requests.csv`, `resources.csv` — seed data
- `database/DatabaseManager.java` — SQLite connection management
- `database/SchemaInitializer.java` — creates tables programmatically
- `database/CSVDataLoader.java` — reads CSV files and batch-inserts into SQLite
- `database/DataAccessObject.java` — CRUD operations returning CustomDynamicArray

**What to demonstrate:**
- Database loads successfully with all 50 locations, 100 roads, 300 requests, 30 taxis
- Records can be queried, inserted, and updated at runtime
- Show SQL schema and sample records in report

---

## M3: Custom Data-Structure Library

**Assigned to:** L200 (linear structures) + L300 (advanced structures)

### L200 Structures (DSA I Focus)
| Structure | File | Key Methods | Evidence Needed |
|-----------|------|-------------|----------------|
| Dynamic Array | `CustomDynamicArray.java` | insert, get, set, remove, resize | Unit tests + resize trace |
| Stack | `CustomStack.java` | push, pop, peek, isEmpty | Undo log demonstration |
| Queue | `CustomQueue.java` | enqueue, dequeue, peek | FIFO dispatch trace |
| Circular Queue | `CustomCircularQueue.java` | enqueue, dequeue, wrap-around | Front/rear pointer trace |
| Deque | `CustomDeque.java` | addFront, addRear, removeFront, removeRear | Urgent insertion example |

### L300 Structures (DSA II Focus)
| Structure | File | Key Methods | Evidence Needed |
|-----------|------|-------------|----------------|
| Singly Linked List | `CustomSinglyLinkedList.java` | addFirst, addLast, remove, iterator | Diagram + iterator demo |
| Doubly Linked List | `CustomDoublyLinkedList.java` | addFirst, addLast, insertBefore, remove | Forward/backward traversal |
| Max Heap | `CustomMaxHeap.java` | insert, extractMax, heapifyUp/Down | Dispatch order trace with swaps |
| Min Heap | `CustomMinHeap.java` | insert, extractMin, decreaseKey | Dijkstra integration trace |
| BST | `CustomBST.java` | insert, search, inorder traversal | Search path + sorted output |
| Red-Black Tree | `CustomRedBlackTree.java` | insert with rotations/recoloring | Before/after rotation diagrams |
| B-Tree | `CustomBTree.java` | insert with splits, search | Node split explanation |
| Hash Table | `CustomHashTable.java` | put, get, remove, collision chaining | Collision stats at different loads |
| Disjoint Set | `CustomDisjointSet.java` | makeSet, find, union | Kruskal connectivity trace |
| Skip List | `CustomSkipList.java` | insert, search, delete | Level display |
| Set | `CustomSet.java` | add, remove, contains, union, intersection | Membership use case |
| Map | `CustomMap.java` | put, get, remove, containsKey | Lookup use case |

---

## M4: Searching and Sorting Engine

**Assigned to:** L200 (DSA I)

**Relevant code:**
- `algorithms/SearchEngine.java` — linear search and binary search with step counting
- `algorithms/SortEngine.java` — selection sort, insertion sort, merge sort, quick sort

**What to demonstrate:**
- Both search algorithms work correctly on sorted and unsorted data
- Binary search precondition (sorted input) is stated and tested
- All four sorts produce correct output
- Comparison/swap counts are tracked and reported
- Stability discussion for insertion sort vs selection sort
- Recurrence relations for merge sort and quick sort

---

## M5: Service Scheduling Engine

**Assigned to:** L200 (FIFO dispatch) + L300 (priority dispatch)

**Relevant code:**
- `engine/DispatchEngine.java` — the main dispatch coordinator
- `engine/PriorityCalculator.java` — priority scoring formula
- `datastructures/CustomCircularQueue.java` — FIFO dispatch
- `datastructures/CustomMaxHeap.java` — priority dispatch
- `datastructures/CustomDeque.java` — urgent insertion
- `datastructures/CustomStack.java` — undo/cancel trail

**What to demonstrate:**
- FIFO dispatch serves requests in submission order
- Priority dispatch serves highest-priority requests first
- Emergency requests preempt the queue
- Cancellations are recorded on the undo stack
- Undo reverses the last action
- Traces show heap array swaps and queue front/rear movement

---

## M6: Tree and Hash Indexing Engine

**Assigned to:** L300 (DSA II)

**Relevant code:**
- `engine/IndexingEngine.java` — coordinates hash table and BST indexes
- `datastructures/CustomHashTable.java` — O(1) request lookup by ID
- `datastructures/CustomBST.java` — time-range and zone indexing
- `datastructures/CustomRedBlackTree.java` — balanced indexing
- `datastructures/CustomBTree.java` — database index simulation

**What to demonstrate:**
- Hash table achieves O(1) average lookup under load factor < 0.75
- BST supports range queries (e.g., requests between two timestamps)
- Collision statistics at different load factors
- B-Tree node split explanation

---

## M7: Graph Route Engine

**Assigned to:** L300 (DSA II)

**Relevant code:**
- `graph/CampusGraph.java` — adjacency list representation of campus
- `graph/GraphNode.java`, `graph/GraphEdge.java` — graph components
- `algorithms/GraphAlgorithms.java` — BFS, DFS, Dijkstra, Prim, Kruskal
- `engine/RouteEngine.java` — routing wrapper for dispatch context

**What to demonstrate:**
- Adjacency list and adjacency matrix representations
- BFS traversal order from any starting location
- DFS traversal order
- Dijkstra shortest path to UG Hospital with distance table trace
- Prim's MST edge list and total cost
- Kruskal's MST using disjoint set with connectivity trace

---

## M8: Optimisation Engine

**Assigned to:** L300 (DSA II)

**Relevant code:**
- `algorithms/GreedyDispatch.java` — greedy driver-to-request matching
- `algorithms/DynamicProgramming.java` — 0/1 knapsack for request selection

**What to demonstrate:**
- Greedy algorithm assigns drivers to requests
- A clear **counterexample** where greedy produces worse total wait times than DP
- DP knapsack optimizes which requests to serve under capacity constraints
- Memoization/tabulation table printout
- Solution reconstruction from DP table

---

## M9: Testing and Correctness Evidence

**Assigned to:** Joint effort (all team members)

**What is needed (minimum):**
- **40+ unit tests** across all data structures and algorithms
- **6 trace tables:** binary search, insertion/merge/quick sort, Dijkstra, Kruskal/Prim, DP
- **3 proof sketches:** loop invariant for search/sort, induction proof, greedy/DP correctness
- **2 counterexamples:** greedy failure, invalid precondition (e.g., unsorted binary search input)
- **Edge cases:** empty structure, single element, duplicate keys, disconnected graph, queue full/empty, hash collision

**Where to add tests:**
Create a `test/` directory with JUnit or simple main-method test classes.

---

## M10: Empirical Efficiency Lab

**Assigned to:** Joint effort (all team members)

**Relevant code:**
- `algorithms/BenchmarkRunner.java` — timing experiments

**Required experiments:**
| Experiment | Input Sizes | Expected Graph |
|-----------|------------|----------------|
| Search comparison | 100, 500, 1K, 5K, 10K | Linear vs binary search runtime |
| Sorting comparison | 100, 500, 1K, 5K, 10K | Selection, insertion, merge, quick sort |
| Hash table load factor | 100 to 20K keys | Load factor vs collision count/time |
| BST vs balanced tree | Multiple sizes | Height and search time comparison |
| Heap priority dispatch | 100 to 20K requests | Insert/extract operation time |
| Graph algorithms | 50, 100, 200, 500 nodes | BFS/DFS/Dijkstra/MST runtime |

**Steps:**
1. Run menu option **15** to execute benchmarks
2. Run menu option **19** to export results to CSV
3. Plot graphs using Excel, Python (matplotlib), or any plotting tool
4. Include raw timing data and graphs in the report
