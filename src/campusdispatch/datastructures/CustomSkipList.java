package campusdispatch.datastructures;

import java.util.Random;

/**
 * Probabilistic skip list.
 */
public class CustomSkipList<T extends Comparable<T>> {
    private static final int MAX_LEVEL = 16;
    private final Node<T> head;
    private int maxLevel;
    private Random random;

    private static class Node<T> {
        T value;
        Node<T>[] forward;

        @SuppressWarnings("unchecked")
        Node(T value, int level) {
            this.value = value;
            this.forward = new Node[level + 1];
        }
    }

    public CustomSkipList() {
        head = new Node<>(null, MAX_LEVEL);
        maxLevel = 0;
        random = new Random();
    }

    private int randomLevel() {
        int lvl = 0;
        while (random.nextFloat() < 0.5f && lvl < MAX_LEVEL) {
            lvl++;
        }
        return lvl;
    }

    @SuppressWarnings("unchecked")
    public void insert(T value) {
        Node<T>[] update = new Node[MAX_LEVEL + 1];
        Node<T> current = head;

        for (int i = maxLevel; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value.compareTo(value) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }
        current = current.forward[0];

        if (current == null || !current.value.equals(value)) {
            int rlevel = randomLevel();
            if (rlevel > maxLevel) {
                for (int i = maxLevel + 1; i <= rlevel; i++) {
                    update[i] = head;
                }
                maxLevel = rlevel;
            }

            Node<T> n = new Node<>(value, rlevel);
            for (int i = 0; i <= rlevel; i++) {
                n.forward[i] = update[i].forward[i];
                update[i].forward[i] = n;
            }
        }
    }

    public boolean search(T value) {
        Node<T> current = head;
        for (int i = maxLevel; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value.compareTo(value) < 0) {
                current = current.forward[i];
            }
        }
        current = current.forward[0];
        return current != null && current.value.equals(value);
    }

    @SuppressWarnings("unchecked")
    public void delete(T value) {
        Node<T>[] update = new Node[MAX_LEVEL + 1];
        Node<T> current = head;

        for (int i = maxLevel; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value.compareTo(value) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }
        current = current.forward[0];

        if (current != null && current.value.equals(value)) {
            for (int i = 0; i <= maxLevel; i++) {
                if (update[i].forward[i] != current) break;
                update[i].forward[i] = current.forward[i];
            }
            while (maxLevel > 0 && head.forward[maxLevel] == null) {
                maxLevel--;
            }
        }
    }

    public void display() {
        for (int i = maxLevel; i >= 0; i--) {
            Node<T> node = head.forward[i];
            System.out.print("Level " + i + ": ");
            while (node != null) {
                System.out.print(node.value + " ");
                node = node.forward[i];
            }
            System.out.println();
        }
    }
}
