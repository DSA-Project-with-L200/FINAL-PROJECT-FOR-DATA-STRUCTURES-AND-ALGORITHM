package campusdispatch.datastructures;

/**
 * B-Tree of configurable order to simulate DB index pages.
 */
public class CustomBTree<T extends Comparable<T>> {
    private int T_DEGREE; // Minimum degree
    
    private class Node {
        int n;
        Object[] keys;
        Node[] children;
        boolean leaf;
        
        Node(boolean leaf) {
            this.leaf = leaf;
            keys = new Object[2 * T_DEGREE - 1];
            children = new CustomBTree.Node[2 * T_DEGREE];
            n = 0;
        }
    }
    
    private Node root;
    
    public CustomBTree(int t) {
        this.T_DEGREE = t;
        root = new Node(true);
    }
    
    public void traverse() {
        if (root != null) traverseRec(root);
        System.out.println();
    }
    
    private void traverseRec(Node node) {
        int i;
        for (i = 0; i < node.n; i++) {
            if (!node.leaf) {
                traverseRec(node.children[i]);
            }
            System.out.print(node.keys[i] + " ");
        }
        if (!node.leaf) {
            traverseRec(node.children[i]);
        }
    }
    
    public Node search(T k) {
        return (root == null) ? null : searchRec(root, k);
    }
    
    @SuppressWarnings("unchecked")
    private Node searchRec(Node node, T k) {
        int i = 0;
        while (i < node.n && k.compareTo((T)node.keys[i]) > 0) i++;
        if (i < node.n && k.compareTo((T)node.keys[i]) == 0) return node;
        if (node.leaf) return null;
        return searchRec(node.children[i], k);
    }
    
    public void insert(T k) {
        Node r = root;
        if (r.n == 2 * T_DEGREE - 1) {
            Node s = new Node(false);
            root = s;
            s.children[0] = r;
            splitChild(s, 0, r);
            insertNonFull(s, k);
        } else {
            insertNonFull(r, k);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void insertNonFull(Node x, T k) {
        int i = x.n - 1;
        if (x.leaf) {
            while (i >= 0 && k.compareTo((T)x.keys[i]) < 0) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = k;
            x.n = x.n + 1;
        } else {
            while (i >= 0 && k.compareTo((T)x.keys[i]) < 0) {
                i--;
            }
            i++;
            if (x.children[i].n == 2 * T_DEGREE - 1) {
                splitChild(x, i, x.children[i]);
                if (k.compareTo((T)x.keys[i]) > 0) {
                    i++;
                }
            }
            insertNonFull(x.children[i], k);
        }
    }
    
    private void splitChild(Node x, int i, Node y) {
        Node z = new Node(y.leaf);
        z.n = T_DEGREE - 1;
        for (int j = 0; j < T_DEGREE - 1; j++) {
            z.keys[j] = y.keys[j + T_DEGREE];
        }
        if (!y.leaf) {
            for (int j = 0; j < T_DEGREE; j++) {
                z.children[j] = y.children[j + T_DEGREE];
            }
        }
        y.n = T_DEGREE - 1;
        for (int j = x.n; j >= i + 1; j--) {
            x.children[j + 1] = x.children[j];
        }
        x.children[i + 1] = z;
        for (int j = x.n - 1; j >= i; j--) {
            x.keys[j + 1] = x.keys[j];
        }
        x.keys[i] = y.keys[T_DEGREE - 1];
        x.n = x.n + 1;
    }
}
