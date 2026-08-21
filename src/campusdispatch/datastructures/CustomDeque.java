package campusdispatch.datastructures;

/**
 * Generic double-ended queue.
 */
public class CustomDeque<T> {
    private CustomDoublyLinkedList<T> list;

    public CustomDeque() {
        list = new CustomDoublyLinkedList<>();
    }

    public void addFront(T element) { list.addFirst(element); }
    public void addRear(T element) { list.addLast(element); }
    public T removeFront() { return list.removeFirst(); }
    public T removeRear() { return list.removeLast(); }

    public T peekFront() { 
        if (list.isEmpty()) throw new IllegalStateException("Deque empty");
        return list.iterator().next(); 
    }
    
    public T peekRear() {
        if (list.isEmpty()) throw new IllegalStateException("Deque empty");
        T last = null;
        for (T item : list) {
            last = item;
        }
        return last;
    }
    
    public boolean isEmpty() { return list.isEmpty(); }
    public int size() { return list.size(); }
    
    /**
     * Gets the number of elements in the deque.
     * 
     * @return the size of the deque
     */
    public int getSize() { return size(); }
    
    /**
     * Gets the front index (always 0 for this deque).
     * 
     * @return 0
     */
    public int getFrontIndex() { return 0; }
    
    /**
     * Gets the rear index (size - 1 for this deque).
     * 
     * @return the rear index
     */
    public int getRearIndex() { return isEmpty() ? 0 : size() - 1; }
}
