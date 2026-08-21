package campusdispatch.engine;

import campusdispatch.datastructures.CustomMaxHeap;
import campusdispatch.datastructures.CustomCircularQueue;
import campusdispatch.datastructures.CustomDeque;
import campusdispatch.datastructures.CustomStack;
import campusdispatch.models.ServiceRequest;
import campusdispatch.TraceLogger;

/**
 * Dispatch Engine for managing and dispatching service requests on campus.
 * Implements a multi-tier queuing system using custom data structures.
 */
public class DispatchEngine {

    // 1. Max Heap for priority-based dispatch
    private final CustomMaxHeap<ServiceRequest> priorityQueue;

    // 2. Circular Queue for FIFO dispatch
    private final CustomCircularQueue<ServiceRequest> fifoQueue;

    // 3. Deque for urgent emergency insertions
    private final CustomDeque<ServiceRequest> urgentDeque;

    // 4. Stack for undo/audit trail
    private final CustomStack<ServiceRequest> undoStack;

    /**
     * Initializes the dispatch engine with appropriate queue capacities.
     */
    public DispatchEngine() {
        // Use a comparator that sorts by priority score descending, keeping MaxHeap logic
        this.priorityQueue = new CustomMaxHeap<>((req1, req2) -> 
            Double.compare(req1.getPriorityScore(), req2.getPriorityScore())
        );
        
        // FIFO queue capacity 500
        this.fifoQueue = new CustomCircularQueue<>(500);
        
        this.urgentDeque = new CustomDeque<>();
        this.undoStack = new CustomStack<>();
    }

    /**
     * Submits a new service request to the engine.
     * Routes to appropriate queue based on category and priority.
     * 
     * @param request the service request to submit
     */
    public void submitRequest(ServiceRequest request) {
        if (request == null) return;

        // Reference PriorityCalculator to compute priority score
        int calculatedPriority = PriorityCalculator.calculatePriority(request);
        
        String category = request.getUserCategory() != null ? request.getUserCategory().toUpperCase() : "";

        // Route to the appropriate queue
        if ("EMERGENCY".equals(category) || "ILL".equals(category) || request.isMedicalUrgency()) {
            urgentDeque.addFront(request);
            System.out.println("Submitted to Urgent Deque: Request " + request.getRequestId());
            TraceLogger.logQueueOperation("Urgent Deque AddFront", 0, urgentDeque.size() - 1);
        } else if (calculatedPriority > 400) { 
            // High priority goes to Max Heap
            priorityQueue.insert(request);
            System.out.println("Submitted to Priority Queue: Request " + request.getRequestId() + " (Score: " + calculatedPriority + ")");
            TraceLogger.logHeapOperation("Heap Insert", "Size: " + priorityQueue.size());
        } else { 
            // Standard priority goes to FIFO queue
            if (!fifoQueue.isFull()) {
                fifoQueue.enqueue(request);
                System.out.println("Submitted to FIFO Queue: Request " + request.getRequestId());
                TraceLogger.logQueueOperation("FIFO Enqueue", 0, fifoQueue.size() - 1);
            } else {
                System.out.println("FIFO Queue is full! Cannot submit standard request " + request.getRequestId());
            }
        }
    }

    /**
     * Dispatches the next request.
     * Hierarchy: Urgent Deque -> Priority Queue -> FIFO Queue.
     * 
     * @return the dispatched request, or null if all queues are empty
     */
    public ServiceRequest dispatchNext() {
        ServiceRequest dispatched = null;

        if (!urgentDeque.isEmpty()) {
            dispatched = urgentDeque.removeFront();
            System.out.println("Dispatched from Urgent Deque: Request " + dispatched.getRequestId());
            TraceLogger.logQueueOperation("Urgent Deque RemoveFront", 0, urgentDeque.size() - 1);
        } else if (!priorityQueue.isEmpty()) {
            dispatched = priorityQueue.extractMax();
            System.out.println("Dispatched from Priority Queue: Request " + dispatched.getRequestId());
            TraceLogger.logHeapOperation("Heap ExtractMax", "Size: " + priorityQueue.size());
        } else if (!fifoQueue.isEmpty()) {
            dispatched = fifoQueue.dequeue();
            System.out.println("Dispatched from FIFO Queue: Request " + dispatched.getRequestId());
            TraceLogger.logQueueOperation("FIFO Dequeue", 0, fifoQueue.size() - 1);
        } else {
            System.out.println("No pending requests to dispatch.");
            return null;
        }

        // Push to audit trail stack
        undoStack.push(dispatched);
        System.out.println("Pushed Request " + dispatched.getRequestId() + " to Undo Stack.");
        return dispatched;
    }

    /**
     * Dispatches the next request purely from the FIFO queue, ignoring priorities.
     * 
     * @return the dispatched request, or null if FIFO queue is empty
     */
    public ServiceRequest dispatchFIFO() {
        if (!fifoQueue.isEmpty()) {
            ServiceRequest dispatched = fifoQueue.dequeue();
            System.out.println("FIFO-only Dispatch: Request " + dispatched.getRequestId());
            TraceLogger.logQueueOperation("FIFO Dequeue", 0, fifoQueue.size() - 1);
            
            undoStack.push(dispatched);
            System.out.println("Pushed Request " + dispatched.getRequestId() + " to Undo Stack.");
            return dispatched;
        }
        
        System.out.println("FIFO Queue is empty. Nothing to dispatch FIFO.");
        return null;
    }

    /**
     * Cancels a pending request by ID.
     * Since we do not have O(1) removal, we rebuild structures sequentially to remove the item.
     * 
     * @param requestId the ID of the request to cancel
     */
    public void cancelRequest(int requestId) {
        boolean cancelled = false;

        // 1. Try removing from Urgent Deque
        if (!urgentDeque.isEmpty()) {
            int initialSize = urgentDeque.size();
            for (int i = 0; i < initialSize; i++) {
                ServiceRequest req = urgentDeque.removeFront();
                if (req.getRequestId() == requestId) {
                    undoStack.push(req);
                    cancelled = true;
                    System.out.println("Cancelled Request " + requestId + " from Urgent Deque.");
                    TraceLogger.logQueueOperation("Urgent Deque Cancel", 0, urgentDeque.size() - 1);
                } else {
                    urgentDeque.addRear(req);
                }
            }
        }

        // 2. Try removing from Priority Queue
        if (!cancelled && !priorityQueue.isEmpty()) {
            CustomMaxHeap<ServiceRequest> tempHeap = new CustomMaxHeap<>((req1, req2) -> 
                Double.compare(req1.getPriorityScore(), req2.getPriorityScore())
            );
            
            while (!priorityQueue.isEmpty()) {
                ServiceRequest req = priorityQueue.extractMax();
                if (req.getRequestId() == requestId) {
                    undoStack.push(req);
                    cancelled = true;
                    System.out.println("Cancelled Request " + requestId + " from Priority Queue.");
                    TraceLogger.logHeapOperation("Heap Cancel", "Size: " + priorityQueue.size());
                } else {
                    tempHeap.insert(req);
                }
            }
            
            // Rebuild the main priority queue
            while (!tempHeap.isEmpty()) {
                priorityQueue.insert(tempHeap.extractMax());
            }
        }

        // 3. Try removing from FIFO Queue
        if (!cancelled && !fifoQueue.isEmpty()) {
            int initialSize = fifoQueue.size();
            for (int i = 0; i < initialSize; i++) {
                ServiceRequest req = fifoQueue.dequeue();
                if (req.getRequestId() == requestId) {
                    undoStack.push(req);
                    cancelled = true;
                    System.out.println("Cancelled Request " + requestId + " from FIFO Queue.");
                    TraceLogger.logQueueOperation("FIFO Cancel", 0, fifoQueue.size() - 1);
                } else {
                    fifoQueue.enqueue(req);
                }
            }
        }

        if (!cancelled) {
            System.out.println("Failed to cancel: Request " + requestId + " not found.");
        }
    }

    /**
     * Undoes the last dispatched or cancelled action.
     * Pops from the undo stack and re-submits the request.
     */
    public void undoLastAction() {
        if (undoStack.isEmpty()) {
            System.out.println("Undo stack is empty. No actions to undo.");
            return;
        }
        
        ServiceRequest lastAction = undoStack.pop();
        System.out.println("Undoing action. Re-submitting Request: " + lastAction.getRequestId());
        submitRequest(lastAction);
    }

    /**
     * Prints the formatted status showing size and first element of each queue.
     */
    public void getQueueStatus() {
        System.out.println("\n--- DISPATCH QUEUE STATUS ---");
        
        // Urgent Deque
        System.out.print("Urgent Deque: Size " + urgentDeque.size());
        if (!urgentDeque.isEmpty()) {
            System.out.println(" | First Element: Request " + urgentDeque.peekFront().getRequestId());
        } else {
            System.out.println();
        }
        
        // Priority Queue
        System.out.print("Priority Queue: Size " + priorityQueue.size());
        if (!priorityQueue.isEmpty()) {
            System.out.println(" | First Element: Request " + priorityQueue.peek().getRequestId());
        } else {
            System.out.println();
        }
        
        // FIFO Queue
        System.out.print("FIFO Queue: Size " + fifoQueue.size());
        if (!fifoQueue.isEmpty()) {
            System.out.println(" | First Element: Request " + fifoQueue.peek().getRequestId());
        } else {
            System.out.println();
        }
        System.out.println("-----------------------------\n");
    }

    /**
     * Returns the size of the undo stack.
     * 
     * @return number of items in undo stack
     */
    public int getUndoStackSize() {
        return undoStack.size();
    }

    /**
     * Returns the total pending requests across all active queues.
     * 
     * @return total pending count
     */
    public int getPendingCount() {
        return urgentDeque.size() + priorityQueue.size() + fifoQueue.size();
    }
}
