package map;

import java.util.Comparator;
import java.util.Random;

public class TreapMap<K, V> {

    private static class Node<K, V> {
        K key;
        V value;
        int priority;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K key, V value, int priority, Node<K, V> parent ) {
            this.key = key;
            this.value = value;
            this.priority = priority;
            this.parent = parent;
        }
    }

    private Node<K, V> root;
    private int size;
    private final Comparator<K> comparator;
    private final Random random;

    public TreapMap() {
        this(null);
    }

    public TreapMap(Comparator<K> comparator) {
        this.comparator = comparator;
        this.random = new Random();
    }

    @SuppressWarnings("unchecked")
    private int compare(K a, K b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<K>) a).compareTo(b);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V get(K key) {
        Node<K, V> current = root;

        while (current != null) {
            int cmp = compare(key, current.key);

            if (cmp == 0) {
                return current.value;
            }  else if (cmp < 0) {
                current = current.left;
            }  else {
                current = current.right;
            }
        }
        return null;
    }

    private void rotateLeft(Node<K, V> x) {
        // y becomes new root of this subtree
        Node<K, V> y = x.right;
        if (y == null) {
            return; // cannot rotate if there is no right child
        }

        // move y's left subtree to x's right
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        // link y to x's parent
        y.parent = x.parent;

        if (x.parent == null) {
            root = y; // x was root
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else  {
            x.parent.right = y;
        }
        // put x as left child of y
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node<K, V> x) {
        // y becomes new root of this subtree
        Node<K, V> y = x.left;
        if (y == null) {
            return; // cannot rotate if no left child
        }

        // move y's right subtree to x's left
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }

        // link y to x's parent
        y.parent = x.parent;

        if (x.parent == null) {
            root = y; // x was a root
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        // put x as right child of y
        y.right = x;
        x.parent = y;
    }
}

