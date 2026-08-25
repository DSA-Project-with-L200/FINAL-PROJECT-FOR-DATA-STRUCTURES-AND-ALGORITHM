package campusdispatch.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * CSVDataLoader loads seed dataset CSV files into SQLite database tables.
 * Supports users parent entity and student, guest, driver subtype tables.
 */
public class CSVDataLoader {

    public static void loadAll() {
        try {
            Connection conn = DatabaseManager.getConnection();
            loadAllCSVData(conn);
        } catch (Exception e) {
            System.err.println("Failed to load CSV seed data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadAllCSVData(Connection conn) throws Exception {
        loadLocations(conn, "data/locations.csv");
        loadRoads(conn, "data/roads.csv");
        loadUsers(conn, "data/users.csv");
        loadStudents(conn, "data/students.csv");
        loadGuests(conn, "data/guests.csv");
        loadDrivers(conn, "data/drivers.csv");
        loadServiceRequests(conn, "data/service_requests.csv");
        loadResources(conn, "data/resources.csv");
    }

    public static void loadLocations(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO locations (locationId, name, zone, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setString(2, parts[1].trim());
                pstmt.setString(3, parts[2].trim());
                pstmt.setDouble(4, Double.parseDouble(parts[3].trim()));
                pstmt.setDouble(5, Double.parseDouble(parts[4].trim()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded locations successfully.");
        }
    }

    public static void loadRoads(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO roads (roadId, sourceLocationId, destLocationId, distanceMeters, congestionFactor, isBidirectional) VALUES (?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setInt(2, Integer.parseInt(parts[1].trim()));
                pstmt.setInt(3, Integer.parseInt(parts[2].trim()));
                pstmt.setDouble(4, Double.parseDouble(parts[3].trim()));
                pstmt.setDouble(5, Double.parseDouble(parts[4].trim()));
                pstmt.setInt(6, Boolean.parseBoolean(parts[5].trim()) ? 1 : 0);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded roads successfully.");
        }
    }

    public static void loadUsers(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO users (userId, fullName, userType, email, phone, homeLocationId, hasDisability) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setString(2, parts[1].trim());
                pstmt.setString(3, parts[2].trim());
                pstmt.setString(4, parts[3].trim());
                pstmt.setString(5, parts[4].trim());
                pstmt.setInt(6, Integer.parseInt(parts[5].trim()));
                pstmt.setInt(7, Integer.parseInt(parts[6].trim()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded users parent table seed dataset successfully.");
        }
    }

    public static void loadStudents(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO students (studentId, userId, indexNumber, hallOfResidence, department, academicYear) VALUES (?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setInt(2, Integer.parseInt(parts[1].trim()));
                pstmt.setString(3, parts[2].trim());
                pstmt.setString(4, parts[3].trim());
                pstmt.setString(5, parts[4].trim());
                pstmt.setString(6, parts[5].trim());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded students subtype extension seed dataset successfully.");
        }
    }

    public static void loadGuests(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO guests (guestId, userId, passCode, visitingDepartment, hostPersonName, durationDays) VALUES (?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setInt(2, Integer.parseInt(parts[1].trim()));
                pstmt.setString(3, parts[2].trim());
                pstmt.setString(4, parts[3].trim());
                pstmt.setString(5, parts[4].trim());
                pstmt.setInt(6, Integer.parseInt(parts[5].trim()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded guests subtype extension seed dataset successfully.");
        }
    }

    public static void loadDrivers(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO drivers (driverId, userId, fullName, licenseNumber, vehiclePlate, vehicleType, capacity, homeLocationId, availabilityStatus, isWheelchairAccessible, contactPhone, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 12) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setInt(2, Integer.parseInt(parts[1].trim()));
                pstmt.setString(3, parts[2].trim());
                pstmt.setString(4, parts[3].trim());
                pstmt.setString(5, parts[4].trim());
                pstmt.setString(6, parts[5].trim());
                pstmt.setInt(7, Integer.parseInt(parts[6].trim()));
                pstmt.setInt(8, Integer.parseInt(parts[7].trim()));
                pstmt.setString(9, parts[8].trim());
                pstmt.setInt(10, Integer.parseInt(parts[9].trim()));
                pstmt.setString(11, parts[10].trim());
                pstmt.setDouble(12, Double.parseDouble(parts[11].trim()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded drivers seed dataset successfully.");
        }
    }

    public static void loadServiceRequests(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO service_requests (requestId, userId, requesterName, userCategory, pickupLocationId, destinationLocationId, status, waitTimeMinutes, isMedicalUrgency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 10) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setInt(2, 1);
                pstmt.setString(3, parts[1].trim());
                pstmt.setString(4, parts[3].trim());
                pstmt.setInt(5, Integer.parseInt(parts[4].trim()));
                pstmt.setInt(6, Integer.parseInt(parts[5].trim()));
                pstmt.setString(7, parts[6].trim());
                pstmt.setDouble(8, Double.parseDouble(parts[8].trim()));
                pstmt.setInt(9, Boolean.parseBoolean(parts[9].trim()) ? 1 : 0);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded service requests successfully.");
        }
    }

    public static void loadResources(Connection conn, String filepath) throws Exception {
        if (!new File(filepath).exists()) return;
        String sql = "INSERT INTO resources (resourceId, driverName, vehiclePlate, type, homeLocationId, capacity, availabilityStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                pstmt.setString(2, parts[1].trim());
                pstmt.setString(3, parts[2].trim());
                pstmt.setString(4, parts[3].trim());
                pstmt.setInt(5, Integer.parseInt(parts[4].trim()));
                pstmt.setInt(6, Integer.parseInt(parts[5].trim()));
                pstmt.setString(7, parts[6].trim());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Loaded resources successfully.");
        }
    }
}
