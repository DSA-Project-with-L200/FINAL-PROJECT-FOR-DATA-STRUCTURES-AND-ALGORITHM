package campusdispatch.engine;

import campusdispatch.datastructures.CustomHashTable;
import campusdispatch.datastructures.CustomBST;
import campusdispatch.datastructures.CustomDynamicArray;
import campusdispatch.models.ServiceRequest;

public class IndexingEngine {
    private final CustomHashTable requestMap;
    private final CustomBST timeIndex;
    private final CustomBST zoneIndex;

    public IndexingEngine() {
        this.requestMap = new CustomHashTable(500);
        this.timeIndex = new CustomBST();
        this.zoneIndex = new CustomBST();
    }

    public void buildIndex(CustomDynamicArray requests) {
        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest req = (ServiceRequest) requests.get(i);
            requestMap.put(String.valueOf(req.getId()), req);
        }
    }

    public ServiceRequest lookupById(int requestId) {
        return (ServiceRequest) requestMap.get(String.valueOf(requestId));
    }

    public void searchByTimeRange(long startTime, long endTime) {
        System.out.println("Searching by time range.");
    }

    public void searchByZone(String zone) {
        System.out.println("Searching by zone.");
    }

    public void getCollisionStats() {
        System.out.println("Hash Table Collisions: " + requestMap.getCollisions());
    }
}
