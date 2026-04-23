package main.java.map;
import interfaces.AbstractMap;
import interfaces.Entry;
import interfaces.SortedMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class TreapMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

    private Node<K, V> root;
    private int size;
    private final Comparator<K> comparator;
    private final Random random;

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

    public static class KeyValuePair<K, V> implements Entry<K, V> {
        private K key;
        private V value;

        public KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "<" + key + ", " + value + ">";
        }
    }


    public TreapMap() {
        this(null);
    }

    public TreapMap(Comparator<K> comparator) {
        this.comparator = comparator;
        this.random = new Random();
        this.root = null;
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(K a, K b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<K>) a).compareTo(b);
    }

    private void validateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        compare(key, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public V get(K key) {
        validateKey(key);
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

    @Override
    public V put(K key, V value) {
        validateKey(key);

        if (root == null) {
            root = new Node<>(key, value, random.nextInt(), null);
            size++;
            return null;
        }

        Node<K, V> current = root;
        Node<K, V> parent = null;

        while (current != null) {
            parent = current;
            int cmp = compare(key, current.key);

            if (cmp == 0) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        Node<K, V> newNode = new Node<>(key, value, random.nextInt(), parent);

        if (compare(key, parent.key) < 0) {
            parent.left = newNode;
        } else  {
            parent.right = newNode;
        }

        size++;
        bubbleUp(newNode);
        return null;
    }


    private void bubbleUp(Node<K, V> node) {
        while (node.parent != null && node.priority > node.parent.priority) {
            if (node == node.parent.left) {
                rotateRight(node.parent);
            } else {
                rotateLeft(node.parent);
            }
        }
    }

    @Override
    public V remove(K key) {
        validateKey(key);
        Node<K, V> node = findNode(key);

        if (node == null) {
            return null;
        }

        V oldValue = node.value;

        // Rotate the node down until it becomes a leaf
        while (node.left != null || node.right != null) {
            if (node.left == null) {
                rotateLeft(node);
            } else if (node.right == null) {
                rotateRight(node);
            } else if (node.left.priority > node.right.priority) {
                rotateRight(node);
            } else {
                rotateLeft(node);
            }
        }

        // Remove the leaf
        if (node.parent == null) {
            root = null;
        } else if (node == node.parent.left) {
            node.parent.left = null;
        } else {
            node.parent.right = null;
        }
        size--;
        return oldValue;
    }


    private Node<K, V> findNode(K key) {
        Node<K, V> current = root;

        while (current != null) {
            int cmp = compare(key, current.key);

            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    public ArrayList<KeyValuePair<K, V>> inorder() {
        ArrayList<KeyValuePair<K, V>> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }


    private void inorder(Node<K, V> node, ArrayList<KeyValuePair<K, V>> result) {
        if (node != null) {
            inorder(node.left, result);
            result.add(new KeyValuePair<>(node.key, node.value));
            inorder(node.right, result);
        }
    }

    public boolean containsKey(K key) {
        validateKey(key);
        return findNode(key) != null;
    }

    public void clear() {
        root = null;
        size = 0;
    }


    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> entries = new ArrayList<>();
        buildEntrySet(root, entries);
        return entries;
    }

    private void buildEntrySet(Node<K, V> node, ArrayList<Entry<K, V>> entries) {
        if (node != null) {
            buildEntrySet(node.left, entries);
            entries.add(new KeyValuePair<>(node.key, node.value));
            buildEntrySet(node.right, entries);
        }
    }

    @Override
    public Entry<K, V> firstEntry() {
        if (root == null) {
            return null;
        }

        Node<K, V> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return new KeyValuePair<>(current.key, current.value);
    }

    @Override
    public Entry<K, V> lastEntry() {
        if (root == null) {
            return null;
        }

        Node<K, V> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return new KeyValuePair<>(current.key, current.value);
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
        validateKey(key);
        Node<K, V> current = root;
        Node<K, V> candidate = null;

        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp == 0) {
                return new KeyValuePair<>(current.key, current.value);
            } else if (cmp < 0) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return candidate == null ? null : new KeyValuePair<>(candidate.key, candidate.value);
    }

    @Override
    public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
        validateKey(key);
        Node<K, V> current = root;
        Node<K, V> candidate = null;

        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp == 0) {
                return new KeyValuePair<>(current.key, current.value);
            } else if (cmp < 0) {
                current = current.left;
            } else {
                candidate = current;
                current = current.right;
            }
        }

        return candidate == null ? null : new KeyValuePair<>(candidate.key, candidate.value);
    }

    @Override
    public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
        validateKey(key);
        Node<K, V> current = root;
        Node<K, V> candidate = null;

        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp <= 0) {
                current = current.left;
            } else {
                candidate = current;
                current = current.right;
            }
        }

        return candidate == null ? null : new KeyValuePair<>(candidate.key, candidate.value);
    }

    @Override
    public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
        validateKey(key);
        Node<K, V> current = root;
        Node<K, V> candidate = null;

        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return candidate == null ? null : new KeyValuePair<>(candidate.key, candidate.value);
    }

    @Override
    public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
        validateKey(fromKey);
        validateKey(toKey);

        if (compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException("fromKey must be <= toKey");
        }

        ArrayList<Entry<K, V>> result = new ArrayList<>();
        buildSubMap(root, result, fromKey, toKey);
        return result;
    }

    private void buildSubMap(Node<K, V> node, ArrayList<Entry<K, V>> result, K fromKey, K toKey) {
        if (node == null) {
            return;
        }

        if (compare(node.key, fromKey) >= 0) {
            buildSubMap(node.left, result, fromKey, toKey);
        }

        if (compare(node.key, fromKey) >= 0 && compare(node.key, toKey) < 0) {
            result.add(new KeyValuePair<>(node.key, node.value));
        }

        if (compare(node.key, toKey) < 0) {
            buildSubMap(node.right, result, fromKey, toKey);
        }
    }
}

