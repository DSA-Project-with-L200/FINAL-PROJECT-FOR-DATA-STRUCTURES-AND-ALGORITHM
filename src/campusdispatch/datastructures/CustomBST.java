package campusdispatch.datastructures;

/**
 * Binary Search Tree implementation.
 */
public class CustomBST<T extends Comparable<T>> {
    private class Node {
        T data;
        Node left, right;
        Node(T data) { this.data = data; }
    }

    private Node root;

    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node insertRec(Node root, T data) {
        if (root == null) {
            return new Node(data);
        }
        int cmp = data.compareTo(root.data);
        if (cmp < 0) root.left = insertRec(root.left, data);
        else if (cmp > 0) root.right = insertRec(root.right, data);
        return root;
    }

    public boolean search(T data) {
        return searchRec(root, data) != null;
    }

    private Node searchRec(Node root, T data) {
        if (root == null || root.data.equals(data)) return root;
        if (data.compareTo(root.data) < 0) return searchRec(root.left, data);
        return searchRec(root.right, data);
    }

    public void delete(T data) {
        root = deleteRec(root, data);
    }

    private Node deleteRec(Node root, T data) {
        if (root == null) return root;
        int cmp = data.compareTo(root.data);
        if (cmp < 0) root.left = deleteRec(root.left, data);
        else if (cmp > 0) root.right = deleteRec(root.right, data);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
            root.data = minVal(root.right);
            root.right = deleteRec(root.right, root.data);
        }
        return root;
    }

    private T minVal(Node root) {
        T minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
    }

    public T min() {
        if (root == null) return null;
        return minVal(root);
    }

    public T max() {
        if (root == null) return null;
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.data;
    }

    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node root) {
        if (root == null) return -1;
        return 1 + Math.max(heightRec(root.left), heightRec(root.right));
    }

    public void inorderTraversal() {
        inorderRec(root);
        System.out.println();
    }
    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.data + " ");
            inorderRec(root.right);
        }
    }
    
    public void preorderTraversal() {
        preorderRec(root);
        System.out.println();
    }
    private void preorderRec(Node root) {
        if (root != null) {
            System.out.print(root.data + " ");
            preorderRec(root.left);
            preorderRec(root.right);
        }
    }
    
    public void postorderTraversal() {
        postorderRec(root);
        System.out.println();
    }
    private void postorderRec(Node root) {
        if (root != null) {
            postorderRec(root.left);
            postorderRec(root.right);
            System.out.print(root.data + " ");
        }
    }
}
