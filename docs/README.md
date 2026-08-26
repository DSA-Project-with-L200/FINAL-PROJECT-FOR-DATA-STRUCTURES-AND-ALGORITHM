# UG Campus Dispatch & Optimization System

## Ghana Smart Service Operations Optimizer

A centralized campus mobility and emergency dispatch platform built for the **University of Ghana (Legon)** campus. This system manages taxi dispatches, prioritizes student and emergency requests, computes optimal routes across campus, and provides performance analysis of all underlying algorithms.

---

## What This System Does

The Campus Dispatch system handles five core operations:

1. **Receives service requests** from students, staff, and emergency cases across campus
2. **Prioritizes requests** using a weighted scoring formula (emergencies first, then disabled individuals, then regular students, then staff, then campus guests)
3. **Dispatches verified campus taxis** to pickup locations using shortest-path routing
4. **Manages the campus road network** as a graph to find routes, check connectivity, and detect congestion
5. **Benchmarks algorithm performance** across varying input sizes for empirical analysis

---

## Quick Start Guide

### Option 1: One-Command Run (Recommended)
```bash
# From the project root directory
make run
```

This will automatically:
1. Compile all Java source files into `out/`
2. Check for `campus_dispatch.db` and run database initialization/seeding if missing
3. Launch the interactive 20-option console application

### Option 2: Database Management via Makefile
```bash
make db-init   # Creates SQLite tables and imports all CSV baseline datasets
make db-reset  # Deletes local database and re-seeds from CSV files
make clean     # Removes compiled files and database file
```

---

## Project Structure

```
DSA final project/
│
├── docs/                          # Documentation folder (you are here)
│   ├── README.md                  # This file — project overview
│   ├── NAVIGATION_GUIDE.md        # How to navigate the codebase
│   ├── MODULE_GUIDE.md            # Detailed module-by-module guide
│   ├── DATA_STRUCTURES_GUIDE.md   # Custom data structures reference
│   ├── ALGORITHMS_GUIDE.md        # Algorithms and their usage
│   ├── DATABASE_GUIDE.md          # Database schema and data flow
│   └── TEAM_WORKFLOW.md           # How to work together as a team
│
├── data/                          # Seed data files
│   ├── schema.sql                 # Database table definitions
│   ├── locations.csv              # 50 UG campus locations
│   ├── roads.csv                  # 100 road segments with weights
│   ├── service_requests.csv       # 300 sample service requests
│   └── resources.csv              # 30 campus taxis
│
├── src/campusdispatch/            # Source code (all packages)
│   ├── CampusDispatchApp.java     # Main entry point — the CLI menu
│   ├── TraceLogger.java           # Trace output utility
│   │
│   ├── models/                    # Data model classes
│   │   ├── Location.java          # Campus location entity
│   │   ├── Road.java              # Road segment entity
│   │   ├── ServiceRequest.java    # Dispatch request entity
│   │   ├── Resource.java          # Campus taxi entity
│   │   ├── AuditEvent.java        # System event log entity
│   │   └── AlgorithmRun.java      # Benchmark result entity
│   │
│   ├── datastructures/            # Custom data structures (NO Java collections!)
│   │   ├── CustomDynamicArray.java
│   │   ├── CustomSinglyLinkedList.java
│   │   ├── CustomDoublyLinkedList.java
│   │   ├── CustomStack.java
│   │   ├── CustomQueue.java
│   │   ├── CustomCircularQueue.java
│   │   ├── CustomDeque.java
│   │   ├── CustomMaxHeap.java
│   │   ├── CustomMinHeap.java
│   │   ├── CustomBST.java
│   │   ├── CustomRedBlackTree.java
│   │   ├── CustomBTree.java
│   │   ├── CustomHashTable.java
│   │   ├── CustomDisjointSet.java
│   │   ├── CustomSkipList.java
│   │   ├── CustomSet.java
│   │   └── CustomMap.java
│   │
│   ├── algorithms/                # Algorithm implementations
│   │   ├── SearchEngine.java      # Linear and binary search
│   │   ├── SortEngine.java        # Selection, insertion, merge, quick sort
│   │   ├── GraphAlgorithms.java   # BFS, DFS, Dijkstra, Prim, Kruskal
│   │   ├── GreedyDispatch.java    # Greedy assignment + counterexample
│   │   ├── DynamicProgramming.java # Knapsack allocation
│   │   └── BenchmarkRunner.java   # Performance timing experiments
│   │
│   ├── database/                  # Database access layer
│   │   ├── DatabaseManager.java   # SQLite connection management
│   │   ├── SchemaInitializer.java # Table creation
│   │   ├── CSVDataLoader.java     # CSV import into database
│   │   └── DataAccessObject.java  # CRUD operations
│   │
│   ├── engine/                    # Service logic engines
│   │   ├── DispatchEngine.java    # Queue-based dispatch coordinator
│   │   ├── PriorityCalculator.java # Priority scoring formula
│   │   ├── IndexingEngine.java    # Hash table + BST indexing
│   │   └── RouteEngine.java       # Shortest path and routing
│   │
│   └── graph/                     # Campus graph model
│       ├── GraphNode.java         # Location node
│       ├── GraphEdge.java         # Road edge
│       └── CampusGraph.java       # Full campus network graph
│
├── lib/                           # External JARs (SQLite JDBC only)
└── out/                           # Compiled .class files
```

---

## The Console Menu

When you run the application, you will see an interactive menu with 20 options:

| Option | Action | What It Demonstrates |
|--------|--------|---------------------|
| 1 | Load/Reload Database | DB integration, CSV parsing |
| 2 | View All Locations | Dynamic array traversal |
| 3 | View All Service Requests | Data retrieval and display |
| 4 | Submit New Request | Priority calculation, heap insert |
| 5 | Dispatch Next (Priority) | Max-heap extract, priority queue |
| 6 | Dispatch Next (FIFO) | Circular queue dequeue |
| 7 | Cancel Request | Stack push (undo trail) |
| 8 | Undo Last Action | Stack pop and reversal |
| 9 | Find Shortest Route | Dijkstra's algorithm |
| 10 | Check Reachability (BFS) | Breadth-first search |
| 11 | View MST | Kruskal/Prim MST |
| 12 | Search Requests | Linear + binary search |
| 13 | Sort Requests | Merge sort, quick sort, etc. |
| 14 | View Queue Status | Queue pointer inspection |
| 15 | Run Benchmarks | Empirical timing experiments |
| 16 | View Audit Log | Stack-based event history |
| 17 | Greedy vs DP Comparison | Counterexample demonstration |
| 18 | Hash Table Statistics | Collision analysis |
| 19 | Export Benchmark CSV | CSV result export |
| 20 | Exit | Clean shutdown |

---

## Priority Scoring Formula

Every service request is scored using:

```
P(Ri) = (UserCategoryWeight × W_cat) + (MedicalUrgency × W_urg)
       + (WaitTimeMinutes × W_wait) + DestinationBonus
```

| Tier | Category | Base Score | Behavior |
|------|----------|-----------|----------|
| 1A | Emergency / Ill Student | 1000 pts | Immediate preemption |
| 1B | Disabled Student / Staff | 800 pts | Queue jump ahead of standard |
| 2 | Standard UG Student | 400 pts | FIFO within tier |
| 3 | University Staff | 250 pts | Priority over guests, under students |
| 4 | Campus Guest | 100 pts | Processed after staff & students |

**Modifiers:**
- Hospital destination bonus: **+200 pts**
- Anti-starvation aging: **+15 pts/min** waiting
- Tie-breaker: `RequestId % TeamIndexParameter`

---

## Academic Compliance Notes

This project strictly follows the DSA course requirements:

- **Zero external collections** — No `java.util.ArrayList`, `HashMap`, `PriorityQueue`, `LinkedList`, or `Stack` in core logic
- **Database persistence** — All data flows through SQLite via JDBC
- **Index number derivation** — Three parameters are derived from team member index numbers (search for `TEAM INDEX PARAMETER` in the code)
- **Trace generation** — All major algorithms produce step-by-step console traces
- **40+ unit tests** expected (testing framework in place)
- **6+ trace tables** required (Dijkstra, Quick Sort, Binary Search, Heap, etc.)

---

## Team Members

| Name | Index Number | Role |
|------|-------------|------|
| [Member 1] | [Index] | [Role] |
| [Member 2] | [Index] | [Role] |
| [Member 3] | [Index] | [Role] |
| [Member 4] | [Index] | [Role] |

*Replace the placeholders above with your actual team details.*
