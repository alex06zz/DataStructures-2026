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
}

