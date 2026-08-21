# Team Workflow Guide

How to work together as a group, divide responsibilities, and make sure the project comes together smoothly.

---

## Team Setup Checklist

Before any coding begins, make sure everyone has:

- [ ] Java 11+ installed (`java -version` to check)
- [ ] An IDE set up (IntelliJ IDEA, VS Code with Java Extension, Eclipse, or NetBeans)
- [ ] The SQLite JDBC driver downloaded into the `lib/` folder
- [ ] A shared Git repository (GitHub, GitLab, or Bitbucket) or shared folder
- [ ] Read this `docs/` folder — at minimum the README.md and this file
- [ ] Their team member **index numbers** ready (needed for three system parameters)

---

## Replacing Index Number Parameters

Three system parameters must be derived from your actual team member index numbers. Search the codebase for `TEAM INDEX PARAMETER` to find all locations:

```bash
grep -rn "TEAM INDEX PARAMETER" src/
```

Replace the placeholder values with real calculations based on your index numbers. For example:
- **Hash table capacity M:** `(indexNumber1 + indexNumber2) % 97 + 50` → gives a prime-ish table size
- **Priority scale factor:** `indexNumber3 % 10 + 10` → gives a scaling multiplier
- **Road congestion penalty:** `(indexNumber1 % 5 + 1) * 0.5` → gives a congestion weight

Document these derivations in your report.

---

## Work Division by Group Level

### L200 Members (DSA I Focus)

**Your primary modules:** M1, M2, M3 (linear), M4, M5 (FIFO), M9, M10

| Task | Files to Work On | Priority |
|------|-----------------|----------|
| Database setup and CSV loading | `database/*.java`, `data/*.csv`, `data/schema.sql` | Week 1 |
| Dynamic Array implementation | `datastructures/CustomDynamicArray.java` | Week 1 |
| Stack implementation | `datastructures/CustomStack.java` | Week 1 |
| Queue and Circular Queue | `datastructures/CustomQueue.java`, `CustomCircularQueue.java` | Week 1 |
| Deque implementation | `datastructures/CustomDeque.java` | Week 1 |
| Search algorithms | `algorithms/SearchEngine.java` | Week 2 |
| Sort algorithms | `algorithms/SortEngine.java` | Week 2 |
| Unit tests for your structures | `test/` directory | Week 3 |
| Trace tables for search/sort | Report | Week 3 |
| Benchmark runs for search/sort | `algorithms/BenchmarkRunner.java` | Week 4 |

### L300 Members (DSA II Focus)

**Your primary modules:** M3 (advanced), M5 (priority), M6, M7, M8, M9, M10

| Task | Files to Work On | Priority |
|------|-----------------|----------|
| Linked List implementations | `datastructures/CustomSingly/DoublyLinkedList.java` | Week 1 |
| Max/Min Heap (Priority Queue) | `datastructures/CustomMaxHeap.java`, `CustomMinHeap.java` | Week 1 |
| Hash Table with chaining | `datastructures/CustomHashTable.java` | Week 1 |
| BST, Red-Black Tree, B-Tree | `datastructures/CustomBST.java`, etc. | Week 1 |
| Disjoint Set (Union-Find) | `datastructures/CustomDisjointSet.java` | Week 1 |
| Campus Graph (Dijkstra, BFS, DFS) | `graph/*.java`, `algorithms/GraphAlgorithms.java` | Week 2 |
| Priority dispatch engine | `engine/DispatchEngine.java`, `PriorityCalculator.java` | Week 2 |
| Greedy + DP optimization | `algorithms/GreedyDispatch.java`, `DynamicProgramming.java` | Week 2 |
| Indexing engine | `engine/IndexingEngine.java` | Week 2 |
| Graph trace tables | Report | Week 3 |
| Graph benchmarks | `algorithms/BenchmarkRunner.java` | Week 4 |

---

## Weekly Milestones

### Week 1: Foundation
**Goal:** All data structures built and tested, database loading works.

- [ ] Database schema created and CSV data imports successfully
- [ ] All custom data structures compile and pass basic tests
- [ ] Graph model loads from database
- [ ] Each member can explain their assigned data structure

### Week 2: Engines and Algorithms
**Goal:** All algorithms implemented, dispatch system works end-to-end.

- [ ] Search and sort algorithms work on real data
- [ ] Dijkstra finds shortest paths on the campus graph
- [ ] BFS/DFS traverse the graph correctly
- [ ] Priority dispatch assigns correct order
- [ ] FIFO dispatch works with circular queue
- [ ] Greedy and DP algorithms produce results

### Week 3: Testing and Traces
**Goal:** Correctness evidence is complete.

- [ ] 40+ unit tests written and passing
- [ ] 6 trace tables generated (binary search, sort, Dijkstra, Kruskal/Prim, DP)
- [ ] 3 proof sketches written (loop invariant, induction, correctness)
- [ ] 2 counterexamples documented (greedy failure, unsorted binary search)
- [ ] Edge cases tested (empty, single element, duplicates, disconnected graph)

### Week 4: Benchmarks and Defense
**Goal:** Empirical results and report complete.

- [ ] Benchmark experiments run at all required sizes (100 to 50,000)
- [ ] CSV timing results exported
- [ ] Performance graphs plotted
- [ ] Theoretical vs empirical complexity discussed
- [ ] Technical report written with all sections
- [ ] Each member prepared for oral defense (1 data structure + 1 algorithm)
- [ ] 5-8 minute demonstration video recorded

---

## How to Run and Test the System

### Compiling
```bash
# From the project root
javac -cp "lib/*" -d out $(find src -name "*.java")
```

### Running
```bash
java -cp "out:lib/*" campusdispatch.CampusDispatchApp
```

### First-Time Database Load
Select menu option **1** on first run. This creates the SQLite database and imports all CSV data.

### Testing a Specific Feature
| What to Test | Menu Option | What You'll See |
|-------------|-------------|----------------|
| Database loading | 1 | "Database initialized. X locations, Y roads loaded." |
| Priority dispatch | 5 | Highest priority request dispatched with trace |
| FIFO dispatch | 6 | Oldest request dispatched |
| Shortest route | 9 | Dijkstra trace + reconstructed path |
| Searching | 12 | Linear and binary search results with step counts |
| Sorting | 13 | Sorted output with comparison/swap counts |
| Benchmarks | 15 | Timing results for all algorithms |

---

## Report Structure Checklist

Your final report should contain:

- [ ] Cover page (title, team members, UG context)
- [ ] Problem statement and assumptions
- [ ] Dataset description and data dictionary
- [ ] System architecture diagram
- [ ] Data structure implementation section (with diagrams)
- [ ] Algorithm implementation section (with pseudocode + Java snippets)
- [ ] Correctness evidence (trace tables, invariants, proofs, tests)
- [ ] Performance analysis (method, raw data tables, graphs, interpretation)
- [ ] Database integration evidence (schema, sample records, screenshots)
- [ ] Greedy failure counterexample analysis
- [ ] Individual contribution statements
- [ ] References
- [ ] Appendices (full code listings if needed)

---

## Oral Defense Preparation

Each team member must be ready to explain:
1. **One data structure** — how it works, its operations, time complexity, and where it's used in the project
2. **One algorithm** — how it works step by step, its complexity, and the trace output it produces

**Possible examiner questions:**
- "Walk me through how your heap handles insertion."
- "What happens when the hash table load factor exceeds 0.75?"
- "Show me the Dijkstra trace for a path from Pentagon to UG Hospital."
- "Why does greedy fail in your counterexample?"
- "Change the priority weight for disabled students and rerun the dispatch."

**Tip:** Practice explaining your structure/algorithm without looking at the code. Draw diagrams on paper.

---

## Git Workflow (if using version control)

```bash
# Create a feature branch for your module
git checkout -b feature/hash-table

# Make your changes, test them
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" campusdispatch.CampusDispatchApp

# Commit with a descriptive message
git add src/campusdispatch/datastructures/CustomHashTable.java
git commit -m "Implement hash table with chaining and collision stats"

# Push and create a pull request
git push origin feature/hash-table
```

**Branch naming convention:**
- `feature/dynamic-array` — new data structure
- `feature/dijkstra` — new algorithm
- `fix/heap-extract` — bug fix
- `docs/trace-tables` — documentation

---

## Communication Tips

- Hold a **15-minute stand-up** at each meeting: what you did, what you're doing next, what's blocking you
- Use a shared Google Doc or Notion page for the report — don't write it at the last minute
- If you're stuck on a data structure, check the `docs/DATA_STRUCTURES_GUIDE.md` first
- Run the full compilation frequently to catch integration issues early
- Keep the `data/` CSV files consistent — don't change column names without updating the loader
