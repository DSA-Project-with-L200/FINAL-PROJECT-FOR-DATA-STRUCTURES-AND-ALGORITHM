package campusdispatch.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import campusdispatch.database.DatabaseManager;

/**
 * Lightweight HTTP REST API Server for the UG Campus Dispatch System.
 * Uses standard Java com.sun.net.httpserver package with zero external dependencies.
 * Serves real SQLite database records to the React Web Application.
 */
public class WebServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private DatabaseManager dbManager;

    public WebServer(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Enable API Endpoints
            server.createContext("/api/status", new StatusHandler());
            server.createContext("/api/locations", new LocationsHandler());
            server.createContext("/api/roads", new RoadsHandler());
            server.createContext("/api/users", new UsersHandler());
            server.createContext("/api/students", new StudentsHandler());
            server.createContext("/api/guests", new GuestsHandler());
            server.createContext("/api/drivers", new DriversHandler());
            server.createContext("/api/requests", new RequestsHandler());
            server.createContext("/api/resources", new ResourcesHandler());

            server.setExecutor(null); // Default executor
            server.start();
            System.out.println("==================================================================");
            System.out.println(" 🌐 Java Backend REST API Server listening on http://localhost:" + PORT);
            System.out.println("   Connected to SQLite Database: campus_dispatch.db");
            System.out.println("==================================================================");
        } catch (IOException e) {
            System.err.println("Failed to start WebServer on port " + PORT + ": " + e.getMessage());
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String responseJson) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        byte[] bytes = responseJson.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    // --- API HANDLERS ---

    private class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = "{\"status\":\"ONLINE\",\"database\":\"campus_dispatch.db\",\"backend\":\"Java HttpServer\",\"port\":" + PORT + "}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    private class LocationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT locationId, name, zone, latitude, longitude FROM locations ORDER BY locationId ASC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"id\":").append(rs.getInt("locationId")).append(",")
                      .append("\"name\":\"").append(escapeJson(rs.getString("name"))).append("\",")
                      .append("\"zone\":\"").append(escapeJson(rs.getString("zone"))).append("\",")
                      .append("\"lat\":").append(rs.getDouble("latitude")).append(",")
                      .append("\"lng\":").append(rs.getDouble("longitude"))
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class RoadsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT roadId, sourceLocationId, destLocationId, distanceMeters, congestionFactor FROM roads ORDER BY roadId ASC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"roadId\":").append(rs.getInt("roadId")).append(",")
                      .append("\"src\":").append(rs.getInt("sourceLocationId")).append(",")
                      .append("\"dest\":").append(rs.getInt("destLocationId")).append(",")
                      .append("\"distance\":").append(rs.getDouble("distanceMeters")).append(",")
                      .append("\"congestion\":").append(rs.getDouble("congestionFactor"))
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT userId, fullName, userType, email, phone, homeLocationId, hasDisability FROM users ORDER BY userId ASC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"userId\":").append(rs.getInt("userId")).append(",")
                      .append("\"fullName\":\"").append(escapeJson(rs.getString("fullName"))).append("\",")
                      .append("\"userType\":\"").append(escapeJson(rs.getString("userType"))).append("\",")
                      .append("\"email\":\"").append(escapeJson(rs.getString("email"))).append("\",")
                      .append("\"phone\":\"").append(escapeJson(rs.getString("phone"))).append("\",")
                      .append("\"homeLocationId\":").append(rs.getInt("homeLocationId")).append(",")
                      .append("\"hasDisability\":").append(rs.getInt("hasDisability") == 1)
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class StudentsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT s.studentId, s.userId, u.fullName, s.indexNumber, s.hallOfResidence, s.department, s.academicYear FROM students s JOIN users u ON s.userId = u.userId ORDER BY s.studentId ASC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"studentId\":").append(rs.getInt("studentId")).append(",")
                      .append("\"userId\":").append(rs.getInt("userId")).append(",")
                      .append("\"fullName\":\"").append(escapeJson(rs.getString("fullName"))).append("\",")
                      .append("\"indexNumber\":\"").append(escapeJson(rs.getString("indexNumber"))).append("\",")
                      .append("\"hallOfResidence\":\"").append(escapeJson(rs.getString("hallOfResidence"))).append("\",")
                      .append("\"department\":\"").append(escapeJson(rs.getString("department"))).append("\",")
                      .append("\"academicYear\":\"").append(escapeJson(rs.getString("academicYear"))).append("\"")
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class GuestsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT g.guestId, g.userId, u.fullName, g.passCode, g.visitingDepartment, g.hostPersonName, g.durationDays FROM guests g JOIN users u ON g.userId = u.userId ORDER BY g.guestId ASC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"guestId\":").append(rs.getInt("guestId")).append(",")
                      .append("\"userId\":").append(rs.getInt("userId")).append(",")
                      .append("\"fullName\":\"").append(escapeJson(rs.getString("fullName"))).append("\",")
                      .append("\"passCode\":\"").append(escapeJson(rs.getString("passCode"))).append("\",")
                      .append("\"visitingDepartment\":\"").append(escapeJson(rs.getString("visitingDepartment"))).append("\",")
                      .append("\"hostPersonName\":\"").append(escapeJson(rs.getString("hostPersonName"))).append("\",")
                      .append("\"durationDays\":").append(rs.getInt("durationDays"))
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class DriversHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT driverId, userId, fullName, licenseNumber, vehiclePlate, vehicleType, capacity, homeLocationId, availabilityStatus, isWheelchairAccessible, contactPhone, rating FROM drivers ORDER BY driverId ASC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"driverId\":").append(rs.getInt("driverId")).append(",")
                      .append("\"userId\":").append(rs.getInt("userId")).append(",")
                      .append("\"fullName\":\"").append(escapeJson(rs.getString("fullName"))).append("\",")
                      .append("\"licenseNumber\":\"").append(escapeJson(rs.getString("licenseNumber"))).append("\",")
                      .append("\"vehiclePlate\":\"").append(escapeJson(rs.getString("vehiclePlate"))).append("\",")
                      .append("\"vehicleType\":\"").append(escapeJson(rs.getString("vehicleType"))).append("\",")
                      .append("\"capacity\":").append(rs.getInt("capacity")).append(",")
                      .append("\"locationId\":").append(rs.getInt("homeLocationId")).append(",")
                      .append("\"availabilityStatus\":\"").append(escapeJson(rs.getString("availabilityStatus"))).append("\",")
                      .append("\"isWheelchairAccessible\":").append(rs.getInt("isWheelchairAccessible") == 1).append(",")
                      .append("\"contactPhone\":\"").append(escapeJson(rs.getString("contactPhone"))).append("\",")
                      .append("\"rating\":").append(rs.getDouble("rating"))
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class RequestsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT requestId, requesterName, userCategory, pickupLocationId, destinationLocationId, status, waitTimeMinutes FROM service_requests ORDER BY requestId ASC LIMIT 50")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"id\":").append(rs.getInt("requestId")).append(",")
                      .append("\"name\":\"").append(escapeJson(rs.getString("requesterName"))).append("\",")
                      .append("\"category\":\"").append(escapeJson(rs.getString("userCategory"))).append("\",")
                      .append("\"pickupLocation\":").append(rs.getInt("pickupLocationId")).append(",")
                      .append("\"destLocation\":").append(rs.getInt("destinationLocationId")).append(",")
                      .append("\"status\":\"").append(escapeJson(rs.getString("status"))).append("\",")
                      .append("\"waitTime\":").append(rs.getDouble("waitTimeMinutes"))
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private class ResourcesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder("[");
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT resourceId, driverName, vehiclePlate, type, homeLocationId, capacity, availabilityStatus FROM resources ORDER BY resourceId ASC")) {
                
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"id\":").append(rs.getInt("resourceId")).append(",")
                      .append("\"driver\":\"").append(escapeJson(rs.getString("driverName"))).append("\",")
                      .append("\"plate\":\"").append(escapeJson(rs.getString("vehiclePlate"))).append("\",")
                      .append("\"type\":\"").append(escapeJson(rs.getString("type"))).append("\",")
                      .append("\"location\":").append(rs.getInt("homeLocationId")).append(",")
                      .append("\"capacity\":").append(rs.getInt("capacity")).append(",")
                      .append("\"status\":\"").append(escapeJson(rs.getString("availabilityStatus"))).append("\"")
                      .append("}");
                    first = false;
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
