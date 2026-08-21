package campusdispatch.datastructures;

/**
 * A generic resizable array-backed list implementation.
 * Built entirely from scratch without using java.util.ArrayList.
 * 
 * @param <T> the type of elements stored in this array
 */
public class CustomDynamicArray<T> {
    private Object[] data;
    private int size;
    // TEAM INDEX PARAMETER: Replace 11045678 with your actual index number
    private static final int INITIAL_CAPACITY = 11045678 % 20 + 10; // Ensures a reasonable starting capacity

    /**
     * Constructs an empty CustomDynamicArray with an initial capacity.
     */
    public CustomDynamicArray() {
        this(INITIAL_CAPACITY);
    }

    public CustomDynamicArray(int initialCapacity) {
        data = new Object[initialCapacity <= 0 ? 10 : initialCapacity];
        size = 0;
    }

    /**
     * Appends the specified element to the end of this list.
     * @param element element to be appended to this list
     */
    public void add(T element) {
        if (size == data.length) {
            resize();
        }
        data[size++] = element;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    public void insert(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (size == data.length) {
            resize();
        }
        // Shift elements to make room
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    /**
     * Returns the element at the specified position in this list.
     * @param index index of the element to return
     * @return the element at the specified position in this list
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     */
    public void set(int index, T element) {
        checkIndex(index);
        data[index] = element;
    }

    /**
     * Removes the element at the specified position in this list.
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removedElement = (T) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        data[--size] = null; // Clear to let GC do its work
        return removedElement;
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list.
     * @param element element to search for
     * @return the index of the first occurrence, or -1 if not found
     */
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? data[i] == null : element.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the number of elements in this list.
     * @return the number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this list contains no elements.
     * @return true if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Doubles the capacity of the backing array.
     */
    private void resize() {
        int newCapacity = data.length * 2;
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
