package campusdispatch.datastructures;

/**
 * Set built on top of CustomHashTable.
 */
public class CustomSet<T> {
    private CustomHashTable<T, Object> map;
    private static final Object PRESENT = new Object();

    public CustomSet() {
        map = new CustomHashTable<>();
    }

    public void add(T element) {
        map.put(element, PRESENT);
    }

    public void remove(T element) {
        map.remove(element);
    }

    public boolean contains(T element) {
        return map.containsKey(element);
    }

    public int size() {
        return map.size();
    }
    
    public CustomDynamicArray<T> elements() {
        return map.keys();
    }

    public CustomSet<T> union(CustomSet<T> other) {
        CustomSet<T> result = new CustomSet<>();
        CustomDynamicArray<T> myKeys = map.keys();
        for (int i = 0; i < myKeys.size(); i++) {
            result.add(myKeys.get(i));
        }
        CustomDynamicArray<T> otherKeys = other.elements();
        for (int i = 0; i < otherKeys.size(); i++) {
            result.add(otherKeys.get(i));
        }
        return result;
    }

    public CustomSet<T> intersection(CustomSet<T> other) {
        CustomSet<T> result = new CustomSet<>();
        CustomDynamicArray<T> myKeys = map.keys();
        for (int i = 0; i < myKeys.size(); i++) {
            T key = myKeys.get(i);
            if (other.contains(key)) {
                result.add(key);
            }
        }
        return result;
    }
}
