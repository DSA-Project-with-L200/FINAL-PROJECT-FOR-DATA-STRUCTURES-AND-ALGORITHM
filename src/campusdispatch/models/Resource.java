package campusdispatch.models;

/**
 * Represents a resource (e.g., campus taxi or emergency van) available for dispatch.
 */
public class Resource {
    private int resourceId;
    private String driverName;
    private String vehiclePlate;
    private String type;
    private int homeLocationId;
    private int capacity;
    private String availabilityStatus;

    /**
     * Constructs a new Resource.
     * 
     * @param resourceId Unique identifier for the resource.
     * @param driverName Name of the driver.
     * @param vehiclePlate License plate number of the vehicle.
     * @param type The type of the vehicle (e.g., CAMPUS_TAXI).
     * @param homeLocationId ID of the vehicle's home base.
     * @param capacity Passenger capacity.
     * @param availabilityStatus Current status (e.g., AVAILABLE, DISPATCHED).
     */
    public Resource(int resourceId, String driverName, String vehiclePlate, String type, 
                    int homeLocationId, int capacity, String availabilityStatus) {
        this.resourceId = resourceId;
        this.driverName = driverName;
        this.vehiclePlate = vehiclePlate;
        this.type = type;
        this.homeLocationId = homeLocationId;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    public int getResourceId() { return resourceId; }
    public int getId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getHomeLocationId() { return homeLocationId; }
    public int getLocation() { return homeLocationId; }
    public void setHomeLocationId(int homeLocationId) { this.homeLocationId = homeLocationId; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public boolean isAvailable() { return "AVAILABLE".equalsIgnoreCase(availabilityStatus); }
    public void setAvailable(boolean available) { this.availabilityStatus = available ? "AVAILABLE" : "DISPATCHED"; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    @Override
    public String toString() {
        return "Resource{id=" + resourceId + ", driver='" + driverName + "', type='" + type + 
               "', status='" + availabilityStatus + "'}";
    }
}
