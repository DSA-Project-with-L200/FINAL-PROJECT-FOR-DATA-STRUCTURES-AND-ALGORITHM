package campusdispatch.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic doubly linked list.
 */
public class CustomDoublyLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public static class Node<T> {
        public T data;
        public Node<T> prev, next;
        Node(T data) { this.data = data; }
    }

    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void insertBefore(Node<T> nextNode, T element) {
        if (nextNode == null) return;
        Node<T> newNode = new Node<>(element);
        newNode.prev = nextNode.prev;
        newNode.next = nextNode;
        nextNode.prev = newNode;
        if (newNode.prev != null) {
            newNode.prev.next = newNode;
        } else {
            head = newNode;
        }
        size++;
    }

    public void insertAfter(Node<T> prevNode, T element) {
        if (prevNode == null) return;
        Node<T> newNode = new Node<>(element);
        newNode.next = prevNode.next;
        newNode.prev = prevNode;
        prevNode.next = newNode;
        if (newNode.next != null) {
            newNode.next.prev = newNode;
        } else {
            tail = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return data;
    }

    public T removeLast() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        T data = tail.data;
        tail = tail.prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return data;
    }

    public void remove(Node<T> node) {
        if (node == null) return;
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;
        
        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
        
        size--;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;
            public boolean hasNext() { return current != null; }
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T d = current.data;
                current = current.next;
                return d;
            }
        };
    }
}
