package campusdispatch.datastructures;

import java.util.Comparator;

/**
 * Max-heap priority queue for dispatch.
 */
public class CustomMaxHeap<T> {
    private Object[] heap;
    private int size;
    private Comparator<? super T> comparator;
    
    private static final int DEFAULT_CAPACITY = 10;

    public CustomMaxHeap(Comparator<? super T> comparator) {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Constructs a MaxHeap with a specific capacity, assuming elements are Comparable.
     * 
     * @param capacity the initial capacity
     */
    public CustomMaxHeap(int capacity) {
        this.heap = new Object[capacity];
        this.size = 0;
        this.comparator = null; // Relies on elements being Comparable
    }


    public void insert(T element) {
        if (size == heap.length) resize();
        heap[size] = element;
        heapifyUp(size);
        size++;
    }

    @SuppressWarnings("unchecked")
    public T extractMax() {
        if (isEmpty()) throw new IllegalStateException("Heap empty");
        T max = (T) heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) {
            heapifyDown(0);
        }
        return max;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Heap empty");
        return (T) heap[0];
    }

    @SuppressWarnings("unchecked")
    private void heapifyUp(int index) {
        int parent = (index - 1) / 2;
        while (index > 0 && compare((T) heap[index], (T) heap[parent]) > 0) {
            System.out.println("HeapifyUp swap: " + heap[index] + " with " + heap[parent]);
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    @SuppressWarnings("unchecked")
    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < size && compare((T) heap[left], (T) heap[largest]) > 0) {
                largest = left;
            }
            if (right < size && compare((T) heap[right], (T) heap[largest]) > 0) {
                largest = right;
            }

            if (largest != index) {
                System.out.println("HeapifyDown swap: " + heap[index] + " with " + heap[largest]);
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super T>) a).compareTo(b);
    }

    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void resize() {
        Object[] newHeap = new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    public int size() { return size; }
    
    /**
     * Gets the number of elements in the heap.
     * 
     * @return the size of the heap
     */
    public int getSize() { return size; }
    
    public boolean isEmpty() { return size == 0; }
}
