package test.java.map;

import main.java.map.TreapMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreapMapTest {

    @Test
    void size() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        assertEquals(0, treap.size());

        treap.put(10, "A");
        assertEquals(1, treap.size());

        treap.put(20, "B");
        assertEquals(2, treap.size());

        treap.put(10, "Updated");
        assertEquals(2, treap.size()); // size should not increase for duplicate key
    }

    @Test
    void isEmpty() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        assertTrue(treap.isEmpty());

        treap.put(10, "A");
        assertFalse(treap.isEmpty());

        treap.remove(10);
        assertTrue(treap.isEmpty());
    }

    @Test
    void get() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        treap.put(10, "A");
        treap.put(5, "B");
        treap.put(15, "C");

        assertEquals("A", treap.get(10));
        assertEquals("B", treap.get(5));
        assertEquals("C", treap.get(15));
        assertNull(treap.get(99));
    }

    @Test
    void putReturnsOldValueForExistingKey() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        assertNull(treap.put(10, "A"));
        assertEquals("A", treap.put(10, "B"));
        assertEquals("B", treap.get(10));
    }

    @Test
    void remove() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        treap.put(10, "A");
        treap.put(5, "B");
        treap.put(15, "C");

        assertEquals("A", treap.remove(10));
        assertNull(treap.get(10));
        assertEquals(2, treap.size());

        assertNull(treap.remove(100));
        assertEquals(2, treap.size());
    }

    @Test
    void containsKey() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        treap.put(10, "A");

        assertTrue(treap.containsKey(10));
        assertFalse(treap.containsKey(99));
    }

    @Test
    void clear() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        treap.put(10, "A");
        treap.put(20, "B");

        treap.clear();

        assertEquals(0, treap.size());
        assertTrue(treap.isEmpty());
        assertNull(treap.get(10));
    }

    @Test
    void inorderReturnsSortedKeys() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        treap.put(20, "B");
        treap.put(10, "A");
        treap.put(30, "C");
        treap.put(25, "D");

        var result = treap.inorder();

        assertEquals(4, result.size());
        assertEquals(10, result.get(0).getKey());
        assertEquals(20, result.get(1).getKey());
        assertEquals(25, result.get(2).getKey());
        assertEquals(30, result.get(3).getKey());
    }

    @Test
    void multipleInsertDelete() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        for (int i = 0; i < 100; i++) {
            treap.put(i, "Val" + i);
        }

        for (int i = 0; i < 100; i++) {
            assertEquals("Val" + i, treap.get(i));
        }

        for (int i = 0; i < 100; i++) {
            treap.remove(i);
        }

        assertTrue(treap.isEmpty());
    }

    @Test
    void removeFromEmptyTreap() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        assertNull(treap.remove(10));
        assertTrue(treap.isEmpty());
    }

}