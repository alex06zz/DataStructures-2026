package main.java.map;
import interfaces.Entry;


import java.util.ArrayList;
import java.util.Comparator;

public class AVLTree<K, V> extends interfaces.AbstractMap<K, V> implements interfaces.SortedMap<K, V>  {

    // Fields---
    private AVLNode<K, V> root;
    private int size;
    private final Comparator<K> comparator;

    private static class AVLNode<K, V> {
        K key;
        V value;
        int height;
        AVLNode<K, V> left;
        AVLNode<K, V> right;
        AVLNode<K, V> parent;

        AVLNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
            this.left = null;
            this.right = null;
            this.parent = null;
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

    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<K> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

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

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    public V search(K key) {
        AVLNode<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public V get(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K) key;
        return search(k);
    }

    private int height(AVLNode<K, V> node) {
        //return null if node = null return 0 else height
        return node == null ? 0 : node.height;
    }

    private void updateHeight(AVLNode<K, V> node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private int getBalance(AVLNode<K, V> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private boolean isBalanced(AVLNode<K, V> node) {
        if (node == null) return true;
        int balance = getBalance(node);
        return Math.abs(balance) <= 1;
    }

    // Rotation Helpers
    private AVLNode<K, V> rotateRight(AVLNode<K, V> y) {
        AVLNode<K, V> x = y.left;
        AVLNode<K, V> T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update parents
        x.parent = y.parent;
        y.parent = x;
        if (T2 != null) {
            T2.parent = y;
        }

        // Update heights
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode<K, V> rotateLeft(AVLNode<K, V> x) {
        AVLNode<K, V> y = x.right;
        AVLNode<K, V> T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update parents
        y.parent = x.parent;
        x.parent = y;
        if (T2 != null) {
            T2.parent = x;
        }

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Rebalances subtree if needed
    private AVLNode<K, V> rebalance(AVLNode<K, V> node) {
        if (node == null) return null;

        // Update height first
        updateHeight(node);

        // Get balance factor
        int balance = getBalance(node);

        // Left heavy
        if (balance > 1) {
            // Left-Right case (double rotation)
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
                if (node.left != null) node.left.parent = node;
            }
            // Left-Left case (single rotation)
            return rotateRight(node);
        }

        // Right heavy
        if (balance < -1) {
            // Right-Left case (double rotation)
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
                if (node.right != null) node.right.parent = node;
            }
            // Right-Right case (single rotation)
            return rotateLeft(node);
        }

        return node;
    }

    // overloaded insert, calls helper fucntion also called insert
    public V insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        AVLNode<K, V> existing = getNode(key);
        if (existing != null) {
            V oldValue = existing.value;
            existing.value = value;
            return oldValue;
        }

        root = insert(root, key, value, null);
        size++;
        return null;
    }

    @Override
    public V put(K key, V value) {
        return insert(key, value);
    }

    private AVLNode<K, V> insert(AVLNode<K, V> node, K key, V value, AVLNode<K, V> parent) {
        if (node == null) {
            AVLNode<K, V> newNode = new AVLNode<>(key, value);
            newNode.parent = parent;
            return newNode;
        }

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            node.left = insert(node.left, key, value, node);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value, node);
        } else {
            node.value = value;
            return node;
        }

        return rebalance(node);
    }

    // Deletes key, calls helper function also called delete
    public V delete(K key) {
        AVLNode<K, V> node = getNode(key);
        if (node == null) {
            return null;
        }

        V oldValue = node.value;
        root = delete(root, key);
        size--;
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K) key;
        return delete(k);
    }

    private AVLNode<K, V> delete(AVLNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            node.left = delete(node.left, key);
            if (node.left != null) node.left.parent = node;
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
            if (node.right != null) node.right.parent = node;
        } else {
            // Found the node to delete
            if (node.left == null || node.right == null) {
                AVLNode<K, V> temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    return null;
                } else {
                    temp.parent = node.parent;
                    return temp;
                }
            } else {
                AVLNode<K, V> successor = getMin(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = delete(node.right, successor.key);
                if (node.right != null) node.right.parent = node;
            }
        }

        return rebalance(node);
    }

    private AVLNode<K, V> getNode(K key) {
        AVLNode<K, V> current = root;
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

    private AVLNode<K, V> getMin(AVLNode<K, V> node) {
        AVLNode<K, V> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private AVLNode<K, V> getMax(AVLNode<K, V> node) {
        AVLNode<K, V> current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    public ArrayList<KeyValuePair<K, V>> inorder() {
        ArrayList<KeyValuePair<K, V>> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(AVLNode<K, V> node, ArrayList<KeyValuePair<K, V>> result) {
        if (node != null) {
            inorder(node.left, result);
            result.add(new KeyValuePair<>(node.key, node.value));
            inorder(node.right, result);
        }
    }

    //useful methods
    public void clear() {
        root = null;
        size = 0;
    }

    public int getHeight() {
        return height(root);
    }

    public boolean isAVL() {
        return isAVL(root);
    }

    private boolean isAVL(AVLNode<K, V> node) {
        if (node == null) return true;

        int balance = getBalance(node);
        if (Math.abs(balance) > 1) return false;

        return isAVL(node.left) && isAVL(node.right);
    }








    // printing meths
    public void printTree() {
        printTree(root, 0);
    }

    private void printTree(AVLNode<K, V> node, int level) {
        if (node == null) return;

        printTree(node.right, level + 1);

        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println(node.key + "(h=" + node.height + ", bal=" + getBalance(node) + ")");

        printTree(node.left, level + 1);
    }

    public void printInOrder() {
        for (KeyValuePair<K, V> pair : inorder()) {
            System.out.print(pair + " ");
        }
        System.out.println();
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> entries = new ArrayList<>();
        buildEntrySet(root, entries);
        return entries;
    }

    private void buildEntrySet(AVLNode<K, V> node, ArrayList<Entry<K, V>> entries) {
        if (node != null) {
            buildEntrySet(node.left, entries);
            entries.add(new KeyValuePair<>(node.key, node.value));
            buildEntrySet(node.right, entries);
        }
    }

    @Override
    public Entry<K, V> firstEntry() {
        if (root == null) return null;
        AVLNode<K, V> min = getMin(root);
        return new KeyValuePair<>(min.key, min.value);
    }

    @Override
    public Entry<K, V> lastEntry() {
        if (root == null) return null;
        AVLNode<K, V> max = getMax(root);
        return new KeyValuePair<>(max.key, max.value);
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        AVLNode<K, V> current = root;
        AVLNode<K, V> candidate = null;

        while (current != null) {
            int cmp = compare(key, current.key);

            if (cmp == 0) return new KeyValuePair<>(current.key, current.value);
            else if (cmp < 0) {
                candidate = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return candidate == null ? null : new KeyValuePair<>(candidate.key, candidate.value);
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        AVLNode<K, V> current = root;
        AVLNode<K, V> candidate = null;

        while (current != null) {
            int cmp = compare(key, current.key);

            if (cmp == 0) return new KeyValuePair<>(current.key, current.value);
            else if (cmp < 0) {
                current = current.left;
            } else {
                candidate = current;
                current = current.right;
            }
        }

        return candidate == null ? null : new KeyValuePair<>(candidate.key, candidate.value);
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        AVLNode<K, V> current = root;
        AVLNode<K, V> candidate = null;

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
    public Entry<K, V> higherEntry(K key) {
        AVLNode<K, V> current = root;
        AVLNode<K, V> candidate = null;

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
    public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) {
        ArrayList<Entry<K, V>> result = new ArrayList<>();
        buildSubMap(root, result, fromKey, toKey);
        return result;
    }

    private void buildSubMap(AVLNode<K, V> node, ArrayList<Entry<K, V>> result, K fromKey, K toKey) {
        if (node == null) return;

        if (compare(node.key, fromKey) >= 0)
            buildSubMap(node.left, result, fromKey, toKey);

        if (compare(node.key, fromKey) >= 0 && compare(node.key, toKey) < 0)
            result.add(new KeyValuePair<>(node.key, node.value));

        if (compare(node.key, toKey) < 0)
            buildSubMap(node.right, result, fromKey, toKey);
    }

    private void validateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        compare(key, key);
    }
}