# Database Guide

This guide explains the database schema, how data flows in and out of SQLite, and how to work with the data layer.

---

## Overview

The project uses **SQLite** as its database engine, accessed through **JDBC** (`java.sql.*`). SQLite stores everything in a single file called `campus_dispatch.db`, which gets created automatically when you first load the database from the menu.

**Why SQLite?**
- No server installation needed — just a `.jar` file and a `.db` file
- Full SQL support (CREATE, INSERT, SELECT, UPDATE, DELETE)
- JDBC compatible — same API as MySQL/PostgreSQL

---

## Database Schema

The database has **6 tables**. The full DDL is in `data/schema.sql`.

### locations
Stores all 50 UG campus locations.

| Column | Type | Description |
|--------|------|-------------|
| locationId | INTEGER (PK) | Unique location ID |
| name | TEXT | Location name (e.g., "UG Hospital") |
| zone | TEXT | Campus zone (e.g., "Medical", "Residential") |
| latitude | REAL | GPS latitude (~5.65) |
| longitude | REAL | GPS longitude (~-0.19) |

### roads
Stores 100 road segments connecting locations (graph edges).

| Column | Type | Description |
|--------|------|-------------|
| roadId | INTEGER (PK) | Unique road ID |
| sourceLocationId | INTEGER (FK) | Start location |
| destLocationId | INTEGER (FK) | End location |
| distanceMeters | REAL | Road length in meters |
| congestionFactor | REAL | Traffic multiplier (1.0 = normal, 2.0 = heavy) |
| isBidirectional | INTEGER | 1 = two-way, 0 = one-way |

### service_requests
Stores 300 dispatch requests from students and staff.

| Column | Type | Description |
|--------|------|-------------|
| requestId | INTEGER (PK) | Unique request ID |
| requesterName | TEXT | Name of the person requesting |
| requesterIndexNumber | INTEGER | Student/staff index number |
| userCategory | TEXT | EMERGENCY, DISABLED, STUDENT, STAFF, or GUEST |
| pickupLocationId | INTEGER (FK) | Where to pick up |
| destinationLocationId | INTEGER (FK) | Where to go |
| status | TEXT | PENDING, DISPATCHED, COMPLETED, or CANCELLED |
| submissionTimestamp | INTEGER | Unix timestamp of request |
| waitTimeMinutes | REAL | Time waiting in queue |
| isMedicalUrgency | INTEGER | 1 = medical emergency, 0 = not |

### resources
Stores 30 campus taxis.

| Column | Type | Description |
|--------|------|-------------|
| resourceId | INTEGER (PK) | Unique taxi ID |
| driverName | TEXT | Driver's name |
| vehiclePlate | TEXT | Ghana plate format (e.g., GR-1234-21) |
| type | TEXT | CAMPUS_TAXI or EMERGENCY_VAN |
| homeLocationId | INTEGER (FK) | Default parking location |
| capacity | INTEGER | Passenger capacity |
| availabilityStatus | TEXT | AVAILABLE, DISPATCHED, or OFF_DUTY |

### audit_events
Stores system events (cancellations, undo operations, status changes).

| Column | Type | Description |
|--------|------|-------------|
| eventId | INTEGER (PK) | Unique event ID |
| eventType | TEXT | Type of event (CANCEL, UNDO, DISPATCH, etc.) |
| description | TEXT | Human-readable description |
| timestamp | INTEGER | When the event occurred |
| relatedRequestId | INTEGER | Associated request ID |

### algorithm_runs
Stores benchmark timing results.

| Column | Type | Description |
|--------|------|-------------|
| runId | INTEGER (PK) | Unique run ID |
| algorithmName | TEXT | Name of algorithm tested |
| inputSize | INTEGER | Size of input (N) |
| timeNs | INTEGER | Execution time in nanoseconds |
| memoryKb | INTEGER | Memory usage in KB |
| dateRun | INTEGER | When the benchmark ran |

---

## Data Flow

```
┌───────────────────┐
│   CSV Files       │  (data/locations.csv, roads.csv, etc.)
│   (Initial Seed)  │
└────────┬──────────┘
         │ CSVDataLoader.loadAll()
         ▼
┌───────────────────┐
│   SQLite Database  │  (campus_dispatch.db)
│   (Persistent)     │
└────────┬──────────┘
         │ DataAccessObject.getAllLocations(), etc.
         ▼
┌───────────────────┐
│ CustomDynamicArray │  (In-memory Java objects)
│ <Location/Road/   │
│  Request/Resource> │
└────────┬──────────┘
         │ Passed to engines & algorithms
         ▼
┌───────────────────┐
│ CampusGraph,      │  (Graph, Queues, Heaps, Hash Tables)
│ DispatchEngine,   │
│ IndexingEngine    │
└───────────────────┘
```

---

## How the Database Code Works

### Step 1: Connection
`DatabaseManager.java` manages the SQLite connection:
```java
// Gets a connection to the SQLite database file
Connection conn = DatabaseManager.getConnection();
```

### Step 2: Table Creation
`SchemaInitializer.java` drops and recreates all tables:
```java
SchemaInitializer.initialize(conn);
// Runs: DROP TABLE IF EXISTS locations; CREATE TABLE locations (...);
```

### Step 3: CSV Import
`CSVDataLoader.java` reads each CSV and batch-inserts:
```java
CSVDataLoader loader = new CSVDataLoader(conn);
loader.loadLocations("data/locations.csv");   // 50 rows
loader.loadRoads("data/roads.csv");           // 100 rows
loader.loadServiceRequests("data/service_requests.csv"); // 300 rows
loader.loadResources("data/resources.csv");   // 30 rows
```

### Step 4: CRUD Operations
`DataAccessObject.java` provides all read/write methods:
```java
DataAccessObject dao = new DataAccessObject(conn);

// Read all locations into a custom array (NOT java.util.ArrayList)
CustomDynamicArray<Location> locations = dao.getAllLocations();

// Insert a new request
dao.insertRequest(newRequest);

// Update a request's status
dao.updateRequestStatus(42, "DISPATCHED");
```

---

## CSV File Formats

Each CSV file has a header row followed by data rows.

### locations.csv
```
locationId,name,zone,latitude,longitude
1,UG Hospital,Medical,5.6505,-0.1862
2,Night Market,Commercial,5.6490,-0.1850
...
```

### roads.csv
```
roadId,sourceLocationId,destLocationId,distanceMeters,congestionFactor,isBidirectional
1,1,2,450.0,1.2,1
2,2,3,320.0,1.0,1
...
```

### service_requests.csv
```
requestId,requesterName,requesterIndexNumber,userCategory,pickupLocationId,destinationLocationId,status,submissionTimestamp,waitTimeMinutes,isMedicalUrgency
1,Kwame Asante,10912345,EMERGENCY,5,1,PENDING,1691500000,0.0,1
...
```

### resources.csv
```
resourceId,driverName,vehiclePlate,type,homeLocationId,capacity,availabilityStatus
1,Kofi Mensah,GR-1234-21,CAMPUS_TAXI,3,4,AVAILABLE
...
```

---

## Common Database Tasks

### Adding a new location
1. Add a row to `data/locations.csv`
2. Reload database from menu option 1
3. Or: use `DataAccessObject.insertLocation()` at runtime

### Changing the schema
1. Edit `data/schema.sql`
2. Update `SchemaInitializer.java` to match
3. Update the relevant model class in `models/`
4. Update `DataAccessObject` CRUD methods
5. Reload database

### Viewing raw database contents
```bash
# Install sqlite3 command-line tool if not available
sqlite3 campus_dispatch.db

# List tables
.tables

# View location data
SELECT * FROM locations LIMIT 10;

# Count requests by category
SELECT userCategory, COUNT(*) FROM service_requests GROUP BY userCategory;

# Exit
.quit
```
