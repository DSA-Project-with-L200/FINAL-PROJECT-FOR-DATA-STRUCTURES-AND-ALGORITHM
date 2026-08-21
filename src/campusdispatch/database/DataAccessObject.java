package campusdispatch.database;

import campusdispatch.models.Location;
import campusdispatch.models.Road;
import campusdispatch.models.ServiceRequest;
import campusdispatch.models.Resource;
import campusdispatch.models.AuditEvent;
import campusdispatch.models.AlgorithmRun;
import campusdispatch.datastructures.CustomDynamicArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides CRUD operations for the application's entities.
 * Uses CustomDynamicArray instead of java.util collections.
 */
public class DataAccessObject {

    public CustomDynamicArray<Location> getAllLocations() {
        CustomDynamicArray<Location> list = new CustomDynamicArray<>();
        String sql = "SELECT * FROM locations";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Location(
                    rs.getInt("locationId"),
                    rs.getString("name"),
                    rs.getString("zone"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching locations: " + e.getMessage());
        }
        return list;
    }

    public CustomDynamicArray<Road> getAllRoads() {
        CustomDynamicArray<Road> list = new CustomDynamicArray<>();
        String sql = "SELECT * FROM roads";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Road(
                    rs.getInt("roadId"),
                    rs.getInt("sourceLocationId"),
                    rs.getInt("destLocationId"),
                    rs.getDouble("distanceMeters"),
                    rs.getDouble("congestionFactor"),
                    rs.getInt("isBidirectional") == 1
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching roads: " + e.getMessage());
        }
        return list;
    }

    public CustomDynamicArray<ServiceRequest> getAllRequests() {
        CustomDynamicArray<ServiceRequest> list = new CustomDynamicArray<>();
        String sql = "SELECT * FROM service_requests";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ServiceRequest(
                    rs.getInt("requestId"),
                    rs.getString("requesterName"),
                    rs.getInt("requesterIndexNumber"),
                    rs.getString("userCategory"),
                    rs.getInt("pickupLocationId"),
                    rs.getInt("destinationLocationId"),
                    rs.getString("status"),
                    rs.getLong("submissionTimestamp"),
                    rs.getDouble("waitTimeMinutes"),
                    rs.getInt("isMedicalUrgency") == 1
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching requests: " + e.getMessage());
        }
        return list;
    }

    public CustomDynamicArray<Resource> getAllResources() {
        CustomDynamicArray<Resource> list = new CustomDynamicArray<>();
        String sql = "SELECT * FROM resources";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Resource(
                    rs.getInt("resourceId"),
                    rs.getString("driverName"),
                    rs.getString("vehiclePlate"),
                    rs.getString("type"),
                    rs.getInt("homeLocationId"),
                    rs.getInt("capacity"),
                    rs.getString("availabilityStatus")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resources: " + e.getMessage());
        }
        return list;
    }

    public void insertRequest(ServiceRequest request) {
        String sql = "INSERT INTO service_requests (requestId, requesterName, requesterIndexNumber, userCategory, pickupLocationId, destinationLocationId, status, submissionTimestamp, waitTimeMinutes, isMedicalUrgency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.getRequestId());
            pstmt.setString(2, request.getRequesterName());
            pstmt.setInt(3, request.getRequesterIndexNumber());
            pstmt.setString(4, request.getUserCategory());
            pstmt.setInt(5, request.getPickupLocationId());
            pstmt.setInt(6, request.getDestinationLocationId());
            pstmt.setString(7, request.getStatus());
            pstmt.setLong(8, request.getSubmissionTimestamp());
            pstmt.setDouble(9, request.getWaitTimeMinutes());
            pstmt.setInt(10, request.isMedicalUrgency() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting request: " + e.getMessage());
        }
    }

    public void updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE service_requests SET status = ? WHERE requestId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating request status: " + e.getMessage());
        }
    }

    public void insertAuditEvent(AuditEvent event) {
        String sql = "INSERT INTO audit_events (eventType, description, timestamp, relatedRequestId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getEventType());
            pstmt.setString(2, event.getDescription());
            pstmt.setLong(3, event.getTimestamp());
            pstmt.setInt(4, event.getRelatedRequestId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting audit event: " + e.getMessage());
        }
    }

    public void insertAlgorithmRun(AlgorithmRun run) {
        String sql = "INSERT INTO algorithm_runs (algorithmName, inputSize, timeNanoseconds, memoryKb, dateRun) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, run.getAlgorithmName());
            pstmt.setInt(2, run.getInputSize());
            pstmt.setLong(3, run.getTimeNanoseconds());
            pstmt.setLong(4, run.getMemoryKb());
            pstmt.setLong(5, run.getDateRun());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting algorithm run: " + e.getMessage());
        }
    }

    public ServiceRequest getRequestById(int id) {
        String sql = "SELECT * FROM service_requests WHERE requestId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ServiceRequest(
                        rs.getInt("requestId"),
                        rs.getString("requesterName"),
                        rs.getInt("requesterIndexNumber"),
                        rs.getString("userCategory"),
                        rs.getInt("pickupLocationId"),
                        rs.getInt("destinationLocationId"),
                        rs.getString("status"),
                        rs.getLong("submissionTimestamp"),
                        rs.getDouble("waitTimeMinutes"),
                        rs.getInt("isMedicalUrgency") == 1
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching request by ID: " + e.getMessage());
        }
        return null;
    }
}
