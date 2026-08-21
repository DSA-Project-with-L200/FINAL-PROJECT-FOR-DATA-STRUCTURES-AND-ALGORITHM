package campusdispatch.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the SQLite database connection.
 * Implements a simple connection approach for the application.
 */
public class DatabaseManager {
    // Path to the SQLite database file
    private static final String DB_URL = "jdbc:sqlite:campus_dispatch.db";
    private static Connection connection = null;

    /**
     * Retrieves the active database connection.
     * Initializes it if it does not exist.
     * Note: In a production environment with high concurrency, a connection pool
     * like HikariCP would be used here. For this academic project, a single
     * shared connection suffices.
     * 
     * @return Connection to the SQLite database.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Ensure the SQLite JDBC driver is loaded
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
            } catch (ClassNotFoundException e) {
                System.err.println("SQLite JDBC driver not found.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Closes the active database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
