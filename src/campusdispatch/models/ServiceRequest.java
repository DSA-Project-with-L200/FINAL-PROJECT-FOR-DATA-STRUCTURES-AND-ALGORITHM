package campusdispatch.models;

/**
 * Represents a service request for transportation on campus.
 * Implements Comparable to sort by priority score descending.
 */
public class ServiceRequest implements Comparable<ServiceRequest> {
    private int requestId;
    private String requesterName;
    // TEAM INDEX PARAMETER: Replace 11045678 with your actual index number
    private int requesterIndexNumber;
    private String userCategory;
    private int pickupLocationId;
    private int destinationLocationId;
    private String status;
    private long submissionTimestamp;
    private double waitTimeMinutes;
    private boolean isMedicalUrgency;
    private double priorityScore;
    private boolean assigned;

    /**
     * Constructs a new ServiceRequest.
     */
    public ServiceRequest(int requestId, String requesterName, int requesterIndexNumber, 
                          String userCategory, int pickupLocationId, int destinationLocationId, 
                          String status, long submissionTimestamp, double waitTimeMinutes, 
                          boolean isMedicalUrgency) {
        this.requestId = requestId;
        this.requesterName = requesterName;
        this.requesterIndexNumber = requesterIndexNumber;
        this.userCategory = userCategory;
        this.pickupLocationId = pickupLocationId;
        this.destinationLocationId = destinationLocationId;
        this.status = status;
        this.submissionTimestamp = submissionTimestamp;
        this.waitTimeMinutes = waitTimeMinutes;
        this.isMedicalUrgency = isMedicalUrgency;
        calculatePriority();
    }

    /**
     * Calculates the priority score based on the formula provided in the PRD.
     */
    public void calculatePriority() {
        double score = 0.0;
        
        // Base points for category
        if ("EMERGENCY".equalsIgnoreCase(userCategory) || isMedicalUrgency) {
            score += 1000.0;
        } else if ("DISABLED".equalsIgnoreCase(userCategory)) {
            score += 800.0;
        } else if ("STUDENT".equalsIgnoreCase(userCategory)) {
            score += 400.0;
        } else if ("STAFF".equalsIgnoreCase(userCategory)) {
            score += 100.0;
        }

        // Destination bonus for UG Hospital (assuming locationId 1 is Hospital)
        if (destinationLocationId == 1) {
            score += 200.0;
        }

        // Anti-starvation aging
        score += (waitTimeMinutes * 15.0);

        this.priorityScore = score;
    }

    // Getters and Setters

    public int getRequestId() { return requestId; }
    public int getId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }

    public int getRequesterIndexNumber() { return requesterIndexNumber; }
    public void setRequesterIndexNumber(int requesterIndexNumber) { this.requesterIndexNumber = requesterIndexNumber; }

    public String getUserCategory() { return userCategory; }
    public String getCategory() { return userCategory; }
    public void setUserCategory(String userCategory) { this.userCategory = userCategory; }

    public int getPriority() { return (int) priorityScore; }
    public int getExpectedTime() { return (int) Math.max(1, waitTimeMinutes); }

    public int getPickupLocationId() { return pickupLocationId; }
    public int getPickupLocation() { return pickupLocationId; }
    public void setPickupLocationId(int pickupLocationId) { this.pickupLocationId = pickupLocationId; }

    public boolean isAssigned() { return assigned; }
    public void setAssigned(boolean assigned) { this.assigned = assigned; }

    public int getDestinationLocationId() { return destinationLocationId; }
    public void setDestinationLocationId(int destinationLocationId) { this.destinationLocationId = destinationLocationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getSubmissionTimestamp() { return submissionTimestamp; }
    public void setSubmissionTimestamp(long submissionTimestamp) { this.submissionTimestamp = submissionTimestamp; }

    public double getWaitTimeMinutes() { return waitTimeMinutes; }
    public void setWaitTimeMinutes(double waitTimeMinutes) { 
        this.waitTimeMinutes = waitTimeMinutes; 
        calculatePriority();
    }

    public boolean isMedicalUrgency() { return isMedicalUrgency; }
    public void setMedicalUrgency(boolean medicalUrgency) { 
        this.isMedicalUrgency = medicalUrgency; 
        calculatePriority();
    }

    public double getPriorityScore() { return priorityScore; }

    @Override
    public int compareTo(ServiceRequest other) {
        // Sort descending by priority score
        return Double.compare(other.priorityScore, this.priorityScore);
    }

    @Override
    public String toString() {
        return "ServiceRequest{id=" + requestId + ", name='" + requesterName + "', category='" + userCategory + 
               "', status='" + status + "', priority=" + priorityScore + "}";
    }
}
