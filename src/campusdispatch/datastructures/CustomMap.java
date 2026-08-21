package campusdispatch.datastructures;

/**
 * Map built on top of CustomHashTable.
 */
public class CustomMap<K, V> {
    private CustomHashTable<K, V> table;

    public static class Entry<K, V> {
        public K key;
        public V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public CustomMap() {
        table = new CustomHashTable<>();
    }

    public void put(K key, V value) {
        table.put(key, value);
    }

    public V get(K key) {
        return table.get(key);
    }

    public V remove(K key) {
        return table.remove(key);
    }

    public boolean containsKey(K key) {
        return table.containsKey(key);
    }

    public CustomDynamicArray<K> keys() {
        return table.keys();
    }

    public CustomDynamicArray<V> values() {
        return table.values();
    }
    
    public int size() {
        return table.size();
    }
    
    public CustomDynamicArray<Entry<K, V>> entrySet() {
        CustomDynamicArray<K> ks = keys();
        CustomDynamicArray<Entry<K, V>> entries = new CustomDynamicArray<>();
        for(int i = 0; i < ks.size(); i++) {
            K k = ks.get(i);
            entries.add(new Entry<>(k, table.get(k)));
        }
        return entries;
    }
}
