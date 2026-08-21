package campusdispatch.database;

import java.io.File;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Loads seed data from CSV files into the SQLite database.
 */
public class CSVDataLoader {

    public static void loadAll() {
        loadLocations("data/locations.csv");
        loadRoads("data/roads.csv");
        loadServiceRequests("data/service_requests.csv");
        loadResources("data/resources.csv");
    }

    public static void loadLocations(String csvPath) {
        String sql = "INSERT INTO locations (locationId, name, zone, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             Scanner scanner = new Scanner(new File(csvPath))) {
            
            if (scanner.hasNextLine()) scanner.nextLine(); // skip header
            
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 5) {
                    pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                    pstmt.setString(2, parts[1].trim());
                    pstmt.setString(3, parts[2].trim());
                    pstmt.setDouble(4, Double.parseDouble(parts[3].trim()));
                    pstmt.setDouble(5, Double.parseDouble(parts[4].trim()));
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
            System.out.println("Loaded locations successfully.");
        } catch (Exception e) {
            System.err.println("Error loading locations: " + e.getMessage());
        }
    }

    public static void loadRoads(String csvPath) {
        String sql = "INSERT INTO roads (roadId, sourceLocationId, destLocationId, distanceMeters, congestionFactor, isBidirectional) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             Scanner scanner = new Scanner(new File(csvPath))) {
            
            if (scanner.hasNextLine()) scanner.nextLine(); 
            
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 6) {
                    pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                    pstmt.setInt(2, Integer.parseInt(parts[1].trim()));
                    pstmt.setInt(3, Integer.parseInt(parts[2].trim()));
                    pstmt.setDouble(4, Double.parseDouble(parts[3].trim()));
                    pstmt.setDouble(5, Double.parseDouble(parts[4].trim()));
                    pstmt.setInt(6, Boolean.parseBoolean(parts[5].trim()) ? 1 : 0);
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
            System.out.println("Loaded roads successfully.");
        } catch (Exception e) {
            System.err.println("Error loading roads: " + e.getMessage());
        }
    }

    public static void loadServiceRequests(String csvPath) {
        String sql = "INSERT INTO service_requests (requestId, requesterName, requesterIndexNumber, userCategory, pickupLocationId, destinationLocationId, status, submissionTimestamp, waitTimeMinutes, isMedicalUrgency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             Scanner scanner = new Scanner(new File(csvPath))) {
            
            if (scanner.hasNextLine()) scanner.nextLine(); 
            
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 10) {
                    pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                    pstmt.setString(2, parts[1].trim());
                    pstmt.setInt(3, Integer.parseInt(parts[2].trim()));
                    pstmt.setString(4, parts[3].trim());
                    pstmt.setInt(5, Integer.parseInt(parts[4].trim()));
                    pstmt.setInt(6, Integer.parseInt(parts[5].trim()));
                    pstmt.setString(7, parts[6].trim());
                    pstmt.setLong(8, Long.parseLong(parts[7].trim()));
                    pstmt.setDouble(9, Double.parseDouble(parts[8].trim()));
                    pstmt.setInt(10, Boolean.parseBoolean(parts[9].trim()) ? 1 : 0);
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
            System.out.println("Loaded service requests successfully.");
        } catch (Exception e) {
            System.err.println("Error loading service requests: " + e.getMessage());
        }
    }

    public static void loadResources(String csvPath) {
        String sql = "INSERT INTO resources (resourceId, driverName, vehiclePlate, type, homeLocationId, capacity, availabilityStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             Scanner scanner = new Scanner(new File(csvPath))) {
            
            if (scanner.hasNextLine()) scanner.nextLine(); 
            
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 7) {
                    pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                    pstmt.setString(2, parts[1].trim());
                    pstmt.setString(3, parts[2].trim());
                    pstmt.setString(4, parts[3].trim());
                    pstmt.setInt(5, Integer.parseInt(parts[4].trim()));
                    pstmt.setInt(6, Integer.parseInt(parts[5].trim()));
                    pstmt.setString(7, parts[6].trim());
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
            System.out.println("Loaded resources successfully.");
        } catch (Exception e) {
            System.err.println("Error loading resources: " + e.getMessage());
        }
    }
}
