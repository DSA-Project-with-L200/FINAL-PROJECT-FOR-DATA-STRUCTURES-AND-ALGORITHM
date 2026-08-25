
-- UG Campus Dispatch & Optimization System Database Schema
-- Embedded SQLite Database (campus_dispatch.db)

DROP TABLE IF EXISTS audit_events;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS service_requests;
DROP TABLE IF EXISTS drivers;
DROP TABLE IF EXISTS guests;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roads;
DROP TABLE IF EXISTS locations;

-- 1. Campus Locations (50 Nodes across University of Ghana, Legon)
CREATE TABLE locations (
  locationId INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  zone TEXT NOT NULL,
  latitude REAL NOT NULL,
  longitude REAL NOT NULL
);

-- 2. Campus Roads (100 Weighted Edges with real Haversine Google Maps distances)
CREATE TABLE roads (
  roadId INTEGER PRIMARY KEY,
  sourceLocationId INTEGER NOT NULL,
  destLocationId INTEGER NOT NULL,
  distanceMeters REAL NOT NULL,
  congestionFactor REAL DEFAULT 1.0,
  isBidirectional INTEGER DEFAULT 1,
  FOREIGN KEY(sourceLocationId) REFERENCES locations(locationId),
  FOREIGN KEY(destLocationId) REFERENCES locations(locationId)
);

-- 3. Base Parent Users Table (Object-Relational Inheritance Model)
CREATE TABLE users (
  userId INTEGER PRIMARY KEY,
  fullName TEXT NOT NULL,
  userType TEXT NOT NULL, -- 'STUDENT', 'GUEST', 'FACULTY', 'STAFF', 'DRIVER'
  email TEXT,
  phone TEXT,
  homeLocationId INTEGER,
  hasDisability INTEGER DEFAULT 0,
  FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)
);

-- 4. Students Subtype Extension Table
CREATE TABLE students (
  studentId INTEGER PRIMARY KEY AUTOINCREMENT,
  userId INTEGER NOT NULL UNIQUE,
  indexNumber TEXT UNIQUE NOT NULL,
  hallOfResidence TEXT NOT NULL,
  department TEXT NOT NULL,
  academicYear TEXT DEFAULT 'Level 300',
  FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE
);

-- 5. Guests Subtype Extension Table
CREATE TABLE guests (
  guestId INTEGER PRIMARY KEY AUTOINCREMENT,
  userId INTEGER NOT NULL UNIQUE,
  passCode TEXT UNIQUE NOT NULL,
  visitingDepartment TEXT NOT NULL,
  hostPersonName TEXT,
  durationDays INTEGER DEFAULT 1,
  FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE
);

-- 6. Drivers Subtype Extension Table
CREATE TABLE drivers (
  driverId INTEGER PRIMARY KEY AUTOINCREMENT,
  userId INTEGER UNIQUE,
  fullName TEXT NOT NULL,
  licenseNumber TEXT UNIQUE NOT NULL,
  vehiclePlate TEXT NOT NULL,
  vehicleType TEXT NOT NULL,
  capacity INTEGER NOT NULL,
  homeLocationId INTEGER,
  availabilityStatus TEXT NOT NULL,
  isWheelchairAccessible INTEGER DEFAULT 0,
  contactPhone TEXT,
  rating REAL DEFAULT 4.9,
  FOREIGN KEY(userId) REFERENCES users(userId),
  FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)
);

-- 7. Service Requests Table
CREATE TABLE service_requests (
  requestId INTEGER PRIMARY KEY,
  userId INTEGER,
  requesterName TEXT NOT NULL,
  userCategory TEXT NOT NULL,
  pickupLocationId INTEGER NOT NULL,
  destinationLocationId INTEGER NOT NULL,
  status TEXT NOT NULL,
  waitTimeMinutes REAL DEFAULT 0.0,
  isMedicalUrgency INTEGER DEFAULT 0,
  FOREIGN KEY(userId) REFERENCES users(userId),
  FOREIGN KEY(pickupLocationId) REFERENCES locations(locationId),
  FOREIGN KEY(destinationLocationId) REFERENCES locations(locationId)
);

-- 8. Physical Resources / Fleet Table
CREATE TABLE resources (
  resourceId INTEGER PRIMARY KEY,
  driverName TEXT NOT NULL,
  vehiclePlate TEXT NOT NULL,
  type TEXT NOT NULL,
  homeLocationId INTEGER NOT NULL,
  capacity INTEGER NOT NULL,
  availabilityStatus TEXT NOT NULL,
  FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)
);

-- 9. System Audit Log Events Table
CREATE TABLE audit_events (
  eventId INTEGER PRIMARY KEY AUTOINCREMENT,
  eventType TEXT NOT NULL,
  description TEXT NOT NULL,
  timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
  relatedRequestId INTEGER
);
