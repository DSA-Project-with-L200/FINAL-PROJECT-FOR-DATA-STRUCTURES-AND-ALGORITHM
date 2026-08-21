package campusdispatch.datastructures;

/**
 * Generic circular queue with enqueue, dequeue, peek, front/rear pointer tracking, wrap-around handling.
 */
public class CustomCircularQueue<T> {
    private Object[] data;
    private int front, rear, size;
    private int capacity;

    public CustomCircularQueue(int capacity) {
        this.capacity = capacity;
        data = new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public void enqueue(T element) {
        if (isFull()) throw new IllegalStateException("Circular Queue is full");
        rear = (rear + 1) % capacity;
        data[rear] = element;
        size++;
        System.out.println("Enqueued: " + element + ". Front is at: " + front + ", Rear wrapped to: " + rear);
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("Circular Queue is empty");
        T element = (T) data[front];
        data[front] = null;
        front = (front + 1) % capacity;
        size--;
        System.out.println("Dequeued: " + element + ". Front wrapped to: " + front + ", Rear is at: " + rear);
        return element;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Circular Queue is empty");
        return (T) data[front];
    }

    public boolean isFull() { return size == capacity; }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    
    /**
     * Gets the front index of the queue.
     * 
     * @return the front index
     */
    public int getFrontIndex() {
        return front;
    }
    
    /**
     * Gets the rear index of the queue.
     * 
     * @return the rear index
     */
    public int getRearIndex() {
        return rear;
    }
}

