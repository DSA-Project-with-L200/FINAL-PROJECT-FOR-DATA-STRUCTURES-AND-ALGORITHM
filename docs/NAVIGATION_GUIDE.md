# Codebase Navigation Guide

This guide helps every team member quickly find and understand any part of the codebase.

---

## How the Code is Organized

The project follows a **layered architecture**. Each layer handles a specific responsibility, and code flows from top to bottom:

```
┌─────────────────────────────────────────────┐
│     CampusDispatchApp.java (CLI Menu)       │  ← You interact here
├─────────────────────────────────────────────┤
│     engine/ (Dispatch, Priority, Indexing)   │  ← Business logic
├─────────────────────────────────────────────┤
│     algorithms/ (Search, Sort, Graph, DP)    │  ← Algorithm layer
├─────────────────────────────────────────────┤
│     graph/ (CampusGraph, Nodes, Edges)       │  ← Graph model
├─────────────────────────────────────────────┤
│     datastructures/ (All custom DS)          │  ← Foundation layer
├─────────────────────────────────────────────┤
│     database/ (JDBC, SQL, CSV loading)       │  ← Persistence layer
├─────────────────────────────────────────────┤
│     models/ (Location, Request, Resource)    │  ← Data entities
└─────────────────────────────────────────────┘
```

---

## Package-by-Package Walkthrough

### 1. `campusdispatch/` (Root Package)
**Files:** `CampusDispatchApp.java`, `TraceLogger.java`

- **CampusDispatchApp.java** is the main entry point. This is where the program starts. It creates a console menu with 20 options and coordinates all engines.
- **TraceLogger.java** is a utility that prints step-by-step algorithm traces to the console (heap swaps, queue pointer movement, Dijkstra updates).

**When to modify:** If you need to add a new menu option or change how trace output looks.

---

### 2. `campusdispatch/models/`
**Files:** `Location.java`, `Road.java`, `ServiceRequest.java`, `Resource.java`, `AuditEvent.java`, `AlgorithmRun.java`

These are simple data-holder classes (like database rows turned into Java objects). Each has:
- Private fields
- Constructor
- Getters and setters
- `toString()` for printing

**When to modify:** If you need to add a new field to any entity (e.g., adding a "vehicleType" field to Resource).

---

### 3. `campusdispatch/datastructures/`
**Files:** 17 custom data structure implementations

This is the **heart of the DSA project**. Every data structure is built from scratch without using `java.util` collections. Each file is self-contained and well-commented.

| File | What It Is | Where It's Used |
|------|-----------|----------------|
| `CustomDynamicArray.java` | Resizable array-backed list | Everywhere — replaces ArrayList |
| `CustomSinglyLinkedList.java` | Singly linked list | Graph adjacency lists, hash table chains |
| `CustomDoublyLinkedList.java` | Doubly linked list | Complex pointer management |
| `CustomStack.java` | LIFO stack | Undo/audit trail, DFS |
| `CustomQueue.java` | FIFO queue | Standard request queue |
| `CustomCircularQueue.java` | Circular FIFO queue | FIFO dispatch mode |
| `CustomDeque.java` | Double-ended queue | Urgent request insertion |
| `CustomMaxHeap.java` | Max-heap priority queue | Priority dispatch |
| `CustomMinHeap.java` | Min-heap | Dijkstra's algorithm |
| `CustomBST.java` | Binary search tree | Indexing by timestamp |
| `CustomRedBlackTree.java` | Self-balancing BST | Balanced index operations |
| `CustomBTree.java` | B-Tree | Database index simulation |
| `CustomHashTable.java` | Hash table with chaining | O(1) request lookup |
| `CustomDisjointSet.java` | Union-Find | Kruskal's MST |
| `CustomSkipList.java` | Probabilistic skip list | Fast ordered search |
| `CustomSet.java` | Set (on hash table) | Membership checks |
| `CustomMap.java` | Map (on hash table) | Key-value lookups |

**When to modify:** If you need to fix a data structure bug, add a method, or optimize performance.

---

### 4. `campusdispatch/algorithms/`
**Files:** `SearchEngine.java`, `SortEngine.java`, `GraphAlgorithms.java`, `GreedyDispatch.java`, `DynamicProgramming.java`, `BenchmarkRunner.java`

| File | Algorithms Implemented |
|------|----------------------|
| `SearchEngine.java` | Linear search, Binary search (with step counting) |
| `SortEngine.java` | Selection sort, Insertion sort, Merge sort, Quick sort |
| `GraphAlgorithms.java` | BFS, DFS, Dijkstra, Prim's MST, Kruskal's MST |
| `GreedyDispatch.java` | Greedy driver matching + counterexample |
| `DynamicProgramming.java` | 0/1 Knapsack for request selection |
| `BenchmarkRunner.java` | Timing experiments across input sizes |

**When to modify:** If you need to add a new algorithm or fix complexity issues.

---

### 5. `campusdispatch/database/`
**Files:** `DatabaseManager.java`, `SchemaInitializer.java`, `CSVDataLoader.java`, `DataAccessObject.java`

| File | Responsibility |
|------|---------------|
| `DatabaseManager.java` | Opens/closes SQLite connection |
| `SchemaInitializer.java` | Creates all 6 database tables |
| `CSVDataLoader.java` | Reads CSV files and inserts into DB |
| `DataAccessObject.java` | All CRUD operations (get, insert, update) |

**When to modify:** If you need to change the database schema or add new query methods.

---

### 6. `campusdispatch/engine/`
**Files:** `DispatchEngine.java`, `PriorityCalculator.java`, `IndexingEngine.java`, `RouteEngine.java`

| File | Responsibility |
|------|---------------|
| `DispatchEngine.java` | Coordinates dispatch using heap, queues, deque, and stack |
| `PriorityCalculator.java` | Computes priority score for each request |
| `IndexingEngine.java` | Maintains hash table and BST indexes for fast lookup |
| `RouteEngine.java` | Wraps graph algorithms for routing context |

**When to modify:** If you need to change dispatch rules, priority weights, or routing logic.

---

### 7. `campusdispatch/graph/`
**Files:** `GraphNode.java`, `GraphEdge.java`, `CampusGraph.java`

| File | Responsibility |
|------|---------------|
| `GraphNode.java` | A location node with its adjacency list |
| `GraphEdge.java` | A weighted edge (road segment) |
| `CampusGraph.java` | The full campus network graph built from DB data |

**When to modify:** If you need to add new graph features or change how the network is constructed.

---

## How to Find Specific Things

### "Where is the priority formula?"
→ `engine/PriorityCalculator.java`

### "Where does Dijkstra run?"
→ `algorithms/GraphAlgorithms.java` (the algorithm)  
→ `engine/RouteEngine.java` (the wrapper that calls it)

### "Where are the database tables defined?"
→ `data/schema.sql` (SQL file)  
→ `database/SchemaInitializer.java` (Java code that runs it)

### "Where is the main menu?"
→ `CampusDispatchApp.java`

### "Where do I add a new location?"
→ Add it to `data/locations.csv`, then reload the database (menu option 1)

### "Where are heap swap traces printed?"
→ `TraceLogger.java` (the logger)  
→ `datastructures/CustomMaxHeap.java` (the heap that calls the logger)

### "How do I run benchmarks?"
→ Menu option 15, which calls `algorithms/BenchmarkRunner.java`

### "Where is the undo feature?"
→ `engine/DispatchEngine.java` uses `datastructures/CustomStack.java`

---

## How Data Flows Through the System

```
CSV files (data/)
    ↓ CSVDataLoader reads them
SQLite Database (campus_dispatch.db)
    ↓ DataAccessObject queries them
CustomDynamicArray<Location/Road/Request/Resource>
    ↓ Passed to engines
CampusGraph (built from locations + roads)
DispatchEngine (loads requests into queues)
IndexingEngine (builds hash table + BST indexes)
    ↓ User selects menu option
Algorithm runs (search, sort, route, dispatch)
    ↓ Results displayed + audit logged
TraceLogger prints step-by-step output
```

---

## Key Design Decisions

1. **Everything is custom** — We never use `java.util.ArrayList` or similar. Every list, queue, stack, tree, and hash table is hand-built.

2. **The graph is separate from algorithms** — `CampusGraph` holds the data, `GraphAlgorithms` operates on it. This keeps concerns separated.

3. **Engines coordinate, algorithms compute** — `DispatchEngine` decides *what* to do; it calls algorithms that decide *how* to do it.

4. **Database is the single source of truth** — CSV files seed the database once. After that, the database is the authoritative store for all data.

5. **Traces are first-class citizens** — Every algorithm logs its steps through `TraceLogger`. This is required for the academic submission (trace tables).
