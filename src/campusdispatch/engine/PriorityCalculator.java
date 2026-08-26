package campusdispatch.engine;

import campusdispatch.models.ServiceRequest;

/**
 * Calculates priority scores for dispatch.
 */
public class PriorityCalculator {
    public static final int EMERGENCY = 1000;
    public static final int DISABLED = 800;
    public static final int STUDENT = 400;
    public static final int STAFF = 250;
    public static final int GUEST = 100;
    public static final int HOSPITAL_BONUS = 200;
    public static final int AGING_RATE = 15;

    public static int calculatePriority(ServiceRequest request) {
        int priority = 0;
        String category = request.getUserCategory() != null ? request.getUserCategory().toUpperCase() : "";

        if (category.equals("EMERGENCY") || category.equals("ILL")) priority += EMERGENCY;
        else if (category.equals("DISABLED")) priority += DISABLED;
        else if (category.equals("STUDENT")) priority += STUDENT;
        else if (category.equals("STAFF")) priority += STAFF;
        else if (category.equals("GUEST")) priority += GUEST;

        long waitTimeMin = (System.currentTimeMillis() - request.getSubmissionTimestamp()) / 60000;
        priority += (int) (waitTimeMin * AGING_RATE);

        if (request.getDestinationLocationId() == 1) { // Assuming 1 = Hospital
            priority += HOSPITAL_BONUS;
        }

        // TEAM INDEX PARAMETER: Replace 11045678 with your actual index number
        int teamIndexParameter = 11045678; 
        priority += (request.getRequestId() % teamIndexParameter) % 10;

        return priority;
    }
}
