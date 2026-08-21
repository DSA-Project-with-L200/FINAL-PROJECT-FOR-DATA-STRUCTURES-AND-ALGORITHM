CREATE TABLE locations (
  locationId INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  zone TEXT,
  latitude REAL,
  longitude REAL
);

CREATE TABLE roads (
  roadId INTEGER PRIMARY KEY,
  sourceLocationId INTEGER,
  destLocationId INTEGER,
  distanceMeters REAL,
  congestionFactor REAL,
  isBidirectional INTEGER,
  FOREIGN KEY(sourceLocationId) REFERENCES locations(locationId),
  FOREIGN KEY(destLocationId) REFERENCES locations(locationId)
);

CREATE TABLE service_requests (
  requestId INTEGER PRIMARY KEY,
  requesterName TEXT,
  requesterIndexNumber INTEGER,
  userCategory TEXT,
  pickupLocationId INTEGER,
  destinationLocationId INTEGER,
  status TEXT,
  submissionTimestamp INTEGER,
  waitTimeMinutes REAL,
  isMedicalUrgency INTEGER,
  FOREIGN KEY(pickupLocationId) REFERENCES locations(locationId),
  FOREIGN KEY(destinationLocationId) REFERENCES locations(locationId)
);

CREATE TABLE resources (
  resourceId INTEGER PRIMARY KEY,
  driverName TEXT,
  vehiclePlate TEXT,
  type TEXT,
  homeLocationId INTEGER,
  capacity INTEGER,
  availabilityStatus TEXT,
  FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)
);

CREATE TABLE audit_events (
  eventId INTEGER PRIMARY KEY AUTOINCREMENT,
  eventType TEXT,
  description TEXT,
  timestamp INTEGER,
  relatedRequestId INTEGER
);

CREATE TABLE algorithm_runs (
  runId INTEGER PRIMARY KEY AUTOINCREMENT,
  algorithmName TEXT,
  inputSize INTEGER,
  timeNanoseconds INTEGER,
  memoryKb INTEGER,
  dateRun INTEGER
);
