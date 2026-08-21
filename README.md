# UG Campus Dispatch & Optimization System

> **Ghana Smart Service Operations Optimizer**  
> *University of Ghana (Legon) — DSA Semester Project*

A centralized campus mobility and emergency dispatch platform built for the University of Ghana (Legon) campus context. Handles taxi dispatches, priorities emergency and disabled requests, calculates shortest routes (Dijkstra), checks graph reachability (BFS/DFS), and benchmarks algorithm performance.

---

## 🚀 How to Run the App (One-Command Setup)

You can build, setup the database, and launch the application using a single command:

```bash
make run
```

That's it! The system will automatically:
1. Compile all Java source files into `out/`
2. Download/verify the SQLite JDBC driver in `lib/`
3. Initialize the SQLite database (`campus_dispatch.db`) and seed CSV data if not already created
4. Launch the interactive 20-option CLI menu

---

## 🗄️ Database Management Commands

The system uses an embedded SQLite database (`campus_dispatch.db`) backed by baseline CSV data in `data/`:

| Command | Action | Description |
|---------|--------|-------------|
| `make db-init` | Initialize Database | Creates tables and seeds 50 locations, 100 roads, 300 requests, and 30 taxis |
| `make db-reset` | Reset Database | Deletes `campus_dispatch.db` and re-seeds from CSV files |
| `make clean` | Clean Project | Removes compiled `.class` files and the database file |

---

## 💻 All Makefile Commands

```bash
make run        # Build project, seed database (if missing), and run application
make run-app    # Alias for 'make run'
make db-init    # Explicitly run database creation and CSV seed script
make db-reset   # Wipe existing database and reload fresh seed data
make build      # Compile all Java files into out/ without launching
make clean      # Delete out/ directory and campus_dispatch.db
make help       # Display Makefile options and guide
```

---

## 📂 Project Structure

```
DSA final project/
├── Makefile                     # Root Makefile with build, db, and run targets
├── run.sh                       # Executable bash helper script
├── data/                        # Seed datasets & schema DDL
│   ├── schema.sql               # Database table definitions
│   ├── locations.csv            # 50 UG campus locations
│   ├── roads.csv                # 100 weighted road segments
│   ├── service_requests.csv     # 300 sample service requests
│   └── resources.csv            # 30 campus taxis
├── docs/                        # Project documentation & guides
│   ├── README.md                # General project overview
│   ├── NAVIGATION_GUIDE.md      # Codebase navigation
│   ├── MODULE_GUIDE.md          # Module breakdown (M1-M10)
│   ├── DATA_STRUCTURES_GUIDE.md # Custom data structures reference
│   ├── ALGORITHMS_GUIDE.md      # Search, Sort, Graph, Greedy, DP guides
│   ├── DATABASE_GUIDE.md        # Database schema & JDBC guide
│   ├── HUMAN_DATA_COLLECTION_GUIDE.md # Manual human data collection methodology (AI-Resistance)
│   └── TEAM_WORKFLOW.md         # Team milestone & defense prep guide
├── lib/                         # External libraries (sqlite-jdbc-3.42.0.0.jar)
└── src/campusdispatch/          # Core Java package
    ├── CampusDispatchApp.java   # Main application & interactive menu
    ├── TraceLogger.java         # Execution trace logger
    ├── datastructures/          # 18 Custom Data Structures (NO java.util collections)
    ├── algorithms/              # Search, Sort, Graph, Greedy, DP & Benchmarking
    ├── database/                # SQLite Connection, Schema, CSV Loader, DAO, DBSetup
    ├── engine/                  # Dispatch Engine, Priority Calculator, Indexing, Routing
    ├── graph/                   # Campus Graph, Nodes, and Edges
    └── models/                  # Location, Road, ServiceRequest, Resource, AuditEvent
```

---

## 🎓 Academic Compliance

- **Zero External Java Collections:** Custom implementations for ArrayList, HashMap, PriorityQueue, LinkedList, Stack, BST, Red-Black Tree, B-Tree, etc.
- **Database Persistence:** SQLite database integration via JDBC (`java.sql.*`).
- **Index Number Derivation:** Look for `// TEAM INDEX PARAMETER` in the code to customize formula parameters with your student index numbers.
