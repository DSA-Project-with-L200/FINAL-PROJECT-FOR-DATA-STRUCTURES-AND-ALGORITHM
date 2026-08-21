package campusdispatch.datastructures;

/**
 * Generic FIFO queue using an array backend.
 */
public class CustomQueue<T> {
    private Object[] data;
    private int front;
    private int rear;
    private int size;
    
    public CustomQueue(int capacity) {
        data = new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public CustomQueue() {
        this(10);
    }

    public void enqueue(T element) {
        if (isFull()) resize();
        rear = (rear + 1) % data.length;
        data[rear] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        T elem = (T) data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return elem;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        return (T) data[front];
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    public boolean isFull() { return size == data.length; }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
        rear = size - 1;
    }
}
