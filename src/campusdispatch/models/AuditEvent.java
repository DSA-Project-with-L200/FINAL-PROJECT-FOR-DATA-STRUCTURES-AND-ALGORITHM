package campusdispatch.models;

/**
 * Represents an audit event logging actions performed in the system.
 */
public class AuditEvent {
    private int eventId;
    private String eventType;
    private String description;
    private long timestamp;
    private int relatedRequestId;

    /**
     * Constructs a new AuditEvent.
     */
    public AuditEvent(int eventId, String eventType, String description, long timestamp, int relatedRequestId) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.description = description;
        this.timestamp = timestamp;
        this.relatedRequestId = relatedRequestId;
    }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getRelatedRequestId() { return relatedRequestId; }
    public void setRelatedRequestId(int relatedRequestId) { this.relatedRequestId = relatedRequestId; }

    @Override
    public String toString() {
        return "AuditEvent{id=" + eventId + ", type='" + eventType + "', reqId=" + relatedRequestId + "}";
    }
}
