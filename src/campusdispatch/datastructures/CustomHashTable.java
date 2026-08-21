package campusdispatch.datastructures;

/**
 * Hash table using linked-list chaining.
 */
public class CustomHashTable<K, V> {
    private class HashNode<K, V> {
        K key;
        V value;
        HashNode<K, V> next;
        public HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private HashNode<K, V>[] chainArray;
    private int numBuckets;
    private int size;
    private int collisions;
    
    // TEAM INDEX PARAMETER: Replace 11045678 with your actual index number
    private static final int INITIAL_CAPACITY = 11045678 % 50 + 20; 

    @SuppressWarnings("unchecked")
    public CustomHashTable() {
        this(INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public CustomHashTable(int initialCapacity) {
        numBuckets = initialCapacity <= 0 ? 16 : initialCapacity;
        chainArray = new HashNode[numBuckets];
        size = 0;
        collisions = 0;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public int getCollisions() { return collisions; }
    public double loadFactor() { return (1.0 * size) / numBuckets; }
    
    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        int index = hashCode % numBuckets;
        return index < 0 ? index * -1 : index;
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = chainArray[bucketIndex];
        
        if (head != null) {
            collisions++;
        }

        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        size++;
        head = chainArray[bucketIndex];
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = head;
        chainArray[bucketIndex] = newNode;

        if (loadFactor() >= 0.75) {
            rehash();
        }
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = chainArray[bucketIndex];

        while (head != null) {
            if (head.key.equals(key)) return head.value;
            head = head.next;
        }
        return null;
    }

    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = chainArray[bucketIndex];
        HashNode<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) break;
            prev = head;
            head = head.next;
        }

        if (head == null) return null;
        size--;
        
        if (prev != null) prev.next = head.next;
        else chainArray[bucketIndex] = head.next;

        return head.value;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public CustomDynamicArray<K> keys() {
        CustomDynamicArray<K> keysList = new CustomDynamicArray<>();
        for (int i = 0; i < numBuckets; i++) {
            HashNode<K, V> head = chainArray[i];
            while (head != null) {
                keysList.add(head.key);
                head = head.next;
            }
        }
        return keysList;
    }
    
    public CustomDynamicArray<V> values() {
        CustomDynamicArray<V> valsList = new CustomDynamicArray<>();
        for (int i = 0; i < numBuckets; i++) {
            HashNode<K, V> head = chainArray[i];
            while (head != null) {
                valsList.add(head.value);
                head = head.next;
            }
        }
        return valsList;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        CustomDynamicArray<HashNode<K, V>> temp = new CustomDynamicArray<>();
        for (int i = 0; i < numBuckets; i++) {
            HashNode<K, V> head = chainArray[i];
            while (head != null) {
                temp.add(head);
                head = head.next;
            }
        }
        
        chainArray = new HashNode[numBuckets * 2];
        numBuckets = numBuckets * 2;
        size = 0;
        
        for (int i = 0; i < temp.size(); i++) {
            put(temp.get(i).key, temp.get(i).value);
        }
    }
}
