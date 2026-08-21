package campusdispatch.datastructures;

/**
 * Min-heap for Dijkstra's algorithm.
 */
public class CustomMinHeap<T extends Comparable<T>> {
    private static class Node<T> {
        T key;
        int value;
        Node(T key, int value) { this.key = key; this.value = value; }
    }

    private Object[] heap;
    private int size;
    private CustomMap<T, Integer> indexMap; // For fast decrease-key

    public CustomMinHeap(int capacity) {
        heap = new Object[capacity];
        size = 0;
        indexMap = new CustomMap<>();
    }

    public void insert(T key) {
        int val = 0;
        if (key instanceof Graph.NodeDistance) {
            val = ((Graph.NodeDistance) key).distance;
        } else if (key instanceof Graph.MSTEdge) {
            val = ((Graph.MSTEdge) key).weight;
        }
        insert(key, val);
    }

    public void insert(T key, int value) {
        if (size == heap.length) resize();
        Node<T> node = new Node<>(key, value);
        heap[size] = node;
        indexMap.put(key, size);
        heapifyUp(size);
        size++;
    }

    @SuppressWarnings("unchecked")
    public T extractMin() {
        if (isEmpty()) throw new IllegalStateException("Heap empty");
        Node<T> minNode = (Node<T>) heap[0];
        T min = minNode.key;
        
        heap[0] = heap[size - 1];
        if(size > 1) {
             indexMap.put(((Node<T>)heap[0]).key, 0);
        }
        heap[size - 1] = null;
        indexMap.remove(min);
        size--;
        
        if (size > 0) heapifyDown(0);
        return min;
    }

    @SuppressWarnings("unchecked")
    public void decreaseKey(T key, int newValue) {
        Integer idx = indexMap.get(key);
        if (idx == null) return;
        Node<T> node = (Node<T>) heap[idx];
        if (newValue < node.value) {
            node.value = newValue;
            heapifyUp(idx);
        }
    }

    @SuppressWarnings("unchecked")
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            Node<T> node = (Node<T>) heap[index];
            Node<T> pNode = (Node<T>) heap[parent];
            if (node.value < pNode.value) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && ((Node<T>)heap[left]).value < ((Node<T>)heap[smallest]).value) {
                smallest = left;
            }
            if (right < size && ((Node<T>)heap[right]).value < ((Node<T>)heap[smallest]).value) {
                smallest = right;
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        indexMap.put(((Node<T>)heap[i]).key, i);
        indexMap.put(((Node<T>)heap[j]).key, j);
    }

    public boolean contains(T key) { return indexMap.containsKey(key); }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    
    @SuppressWarnings("unchecked")
    public T peek() {
        if(isEmpty()) return null;
        return ((Node<T>)heap[0]).key;
    }
    
    private void resize() {
        Object[] newHeap = new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }
}
