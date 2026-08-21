package campusdispatch.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Initializes the database schema for the campus dispatch system.
 * Drops existing tables and recreates them to ensure a clean state.
 */
public class SchemaInitializer {

    /**
     * Runs the DDL statements to create the required tables.
     */
    public static void initializeDatabase() {
        String[] dropStatements = {
            "DROP TABLE IF EXISTS algorithm_runs;",
            "DROP TABLE IF EXISTS audit_events;",
            "DROP TABLE IF EXISTS resources;",
            "DROP TABLE IF EXISTS service_requests;",
            "DROP TABLE IF EXISTS roads;",
            "DROP TABLE IF EXISTS locations;"
        };

        String[] createStatements = {
            "CREATE TABLE locations (" +
            "  locationId INTEGER PRIMARY KEY," +
            "  name TEXT NOT NULL," +
            "  zone TEXT," +
            "  latitude REAL," +
            "  longitude REAL" +
            ");",

            "CREATE TABLE roads (" +
            "  roadId INTEGER PRIMARY KEY," +
            "  sourceLocationId INTEGER," +
            "  destLocationId INTEGER," +
            "  distanceMeters REAL," +
            "  congestionFactor REAL," +
            "  isBidirectional INTEGER," +
            "  FOREIGN KEY(sourceLocationId) REFERENCES locations(locationId)," +
            "  FOREIGN KEY(destLocationId) REFERENCES locations(locationId)" +
            ");",

            "CREATE TABLE service_requests (" +
            "  requestId INTEGER PRIMARY KEY," +
            "  requesterName TEXT," +
            "  requesterIndexNumber INTEGER," +
            "  userCategory TEXT," +
            "  pickupLocationId INTEGER," +
            "  destinationLocationId INTEGER," +
            "  status TEXT," +
            "  submissionTimestamp INTEGER," +
            "  waitTimeMinutes REAL," +
            "  isMedicalUrgency INTEGER," +
            "  FOREIGN KEY(pickupLocationId) REFERENCES locations(locationId)," +
            "  FOREIGN KEY(destinationLocationId) REFERENCES locations(locationId)" +
            ");",

            "CREATE TABLE resources (" +
            "  resourceId INTEGER PRIMARY KEY," +
            "  driverName TEXT," +
            "  vehiclePlate TEXT," +
            "  type TEXT," +
            "  homeLocationId INTEGER," +
            "  capacity INTEGER," +
            "  availabilityStatus TEXT," +
            "  FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)" +
            ");",

            "CREATE TABLE audit_events (" +
            "  eventId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  eventType TEXT," +
            "  description TEXT," +
            "  timestamp INTEGER," +
            "  relatedRequestId INTEGER" +
            ");",

            "CREATE TABLE algorithm_runs (" +
            "  runId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  algorithmName TEXT," +
            "  inputSize INTEGER," +
            "  timeNanoseconds INTEGER," +
            "  memoryKb INTEGER," +
            "  dateRun INTEGER" +
            ");"
        };

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Drop existing tables
            for (String sql : dropStatements) {
                stmt.execute(sql);
            }
            
            // Create fresh tables
            for (String sql : createStatements) {
                stmt.execute(sql);
            }
            System.out.println("Database schema initialized successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
