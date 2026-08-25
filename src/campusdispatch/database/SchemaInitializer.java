package campusdispatch.database;

import java.sql.Connection;
import java.sql.Statement;

/**
 * SchemaInitializer executes DDL statements to set up SQLite tables.
 * Restructured with parent `users` table and `students`, `guests`, `drivers` subtype extension tables.
 */
public class SchemaInitializer {

    public static void initializeDatabase() {
        try {
            Connection conn = DatabaseManager.getConnection();
            initializeSchema(conn);
        } catch (Exception e) {
            System.err.println("Failed to initialize database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initializeSchema(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            // Drop existing tables in reverse dependency order
            stmt.execute("DROP TABLE IF EXISTS audit_events;");
            stmt.execute("DROP TABLE IF EXISTS resources;");
            stmt.execute("DROP TABLE IF EXISTS service_requests;");
            stmt.execute("DROP TABLE IF EXISTS drivers;");
            stmt.execute("DROP TABLE IF EXISTS guests;");
            stmt.execute("DROP TABLE IF EXISTS students;");
            stmt.execute("DROP TABLE IF EXISTS users;");
            stmt.execute("DROP TABLE IF EXISTS roads;");
            stmt.execute("DROP TABLE IF EXISTS locations;");

            // 1. Campus Locations
            stmt.execute("CREATE TABLE locations (" +
                    "locationId INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "zone TEXT NOT NULL, " +
                    "latitude REAL NOT NULL, " +
                    "longitude REAL NOT NULL" +
                    ");");

            // 2. Campus Roads
            stmt.execute("CREATE TABLE roads (" +
                    "roadId INTEGER PRIMARY KEY, " +
                    "sourceLocationId INTEGER NOT NULL, " +
                    "destLocationId INTEGER NOT NULL, " +
                    "distanceMeters REAL NOT NULL, " +
                    "congestionFactor REAL DEFAULT 1.0, " +
                    "isBidirectional INTEGER DEFAULT 1, " +
                    "FOREIGN KEY(sourceLocationId) REFERENCES locations(locationId), " +
                    "FOREIGN KEY(destLocationId) REFERENCES locations(locationId)" +
                    ");");

            // 3. Parent Users Table
            stmt.execute("CREATE TABLE users (" +
                    "userId INTEGER PRIMARY KEY, " +
                    "fullName TEXT NOT NULL, " +
                    "userType TEXT NOT NULL, " +
                    "email TEXT, " +
                    "phone TEXT, " +
                    "homeLocationId INTEGER, " +
                    "hasDisability INTEGER DEFAULT 0, " +
                    "FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)" +
                    ");");

            // 4. Students Subtype Extension
            stmt.execute("CREATE TABLE students (" +
                    "studentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER NOT NULL UNIQUE, " +
                    "indexNumber TEXT UNIQUE NOT NULL, " +
                    "hallOfResidence TEXT NOT NULL, " +
                    "department TEXT NOT NULL, " +
                    "academicYear TEXT DEFAULT 'Level 300', " +
                    "FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE" +
                    ");");

            // 5. Guests Subtype Extension
            stmt.execute("CREATE TABLE guests (" +
                    "guestId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER NOT NULL UNIQUE, " +
                    "passCode TEXT UNIQUE NOT NULL, " +
                    "visitingDepartment TEXT NOT NULL, " +
                    "hostPersonName TEXT, " +
                    "durationDays INTEGER DEFAULT 1, " +
                    "FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE" +
                    ");");

            // 6. Drivers Subtype Extension
            stmt.execute("CREATE TABLE drivers (" +
                    "driverId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER UNIQUE, " +
                    "fullName TEXT NOT NULL, " +
                    "licenseNumber TEXT UNIQUE NOT NULL, " +
                    "vehiclePlate TEXT NOT NULL, " +
                    "vehicleType TEXT NOT NULL, " +
                    "capacity INTEGER NOT NULL, " +
                    "homeLocationId INTEGER, " +
                    "availabilityStatus TEXT NOT NULL, " +
                    "isWheelchairAccessible INTEGER DEFAULT 0, " +
                    "contactPhone TEXT, " +
                    "rating REAL DEFAULT 4.9, " +
                    "FOREIGN KEY(userId) REFERENCES users(userId), " +
                    "FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)" +
                    ");");

            // 7. Service Requests Table
            stmt.execute("CREATE TABLE service_requests (" +
                    "requestId INTEGER PRIMARY KEY, " +
                    "userId INTEGER, " +
                    "requesterName TEXT NOT NULL, " +
                    "userCategory TEXT NOT NULL, " +
                    "pickupLocationId INTEGER NOT NULL, " +
                    "destinationLocationId INTEGER NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "waitTimeMinutes REAL DEFAULT 0.0, " +
                    "isMedicalUrgency INTEGER DEFAULT 0, " +
                    "FOREIGN KEY(userId) REFERENCES users(userId), " +
                    "FOREIGN KEY(pickupLocationId) REFERENCES locations(locationId), " +
                    "FOREIGN KEY(destinationLocationId) REFERENCES locations(locationId)" +
                    ");");

            // 8. Physical Resources Table
            stmt.execute("CREATE TABLE resources (" +
                    "resourceId INTEGER PRIMARY KEY, " +
                    "driverName TEXT NOT NULL, " +
                    "vehiclePlate TEXT NOT NULL, " +
                    "type TEXT NOT NULL, " +
                    "homeLocationId INTEGER NOT NULL, " +
                    "capacity INTEGER NOT NULL, " +
                    "availabilityStatus TEXT NOT NULL, " +
                    "FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)" +
                    ");");

            // 9. System Audit Log Events Table
            stmt.execute("CREATE TABLE audit_events (" +
                    "eventId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "eventType TEXT NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "timestamp TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "relatedRequestId INTEGER" +
                    ");");

            System.out.println("Database schema initialized successfully with parent users table and student/guest subtype extensions.");
        }
    }
}
