package campusdispatch.datastructures;

/**
 * Generic stack using array backend.
 */
public class CustomStack<T> {
    private Object[] data;
    private int top;
    
    // TEAM INDEX PARAMETER: Replace 11045678 with your actual index number
    private static final int DEFAULT_CAP = 11045678 % 50 + 20;

    public CustomStack() {
        this(DEFAULT_CAP);
    }

    public CustomStack(int capacity) {
        data = new Object[capacity];
        top = -1;
    }

    public void push(T element) {
        if (isFull()) {
            resize();
        }
        data[++top] = element;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        Object element = data[top];
        data[top--] = null;
        return (T) element;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return (T) data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }

    public boolean isFull() {
        return top == data.length - 1;
    }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack[");
        for (int i = 0; i <= top; i++) {
            sb.append(data[i]);
            if (i < top) sb.append(", ");
        }
        sb.append("] (top)");
        return sb.toString();
    }
}
