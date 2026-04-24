package test.java.map;

import main.java.map.TreapMap;
import org.junit.jupiter.api.Test;
import interfaces.Entry;
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
    void entrySetReturnsSortedKeys() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        treap.put(20, "B");
        treap.put(10, "A");
        treap.put(30, "C");
        treap.put(25, "D");

        var result = new java.util.ArrayList<Entry<Integer, String>>();
        for (var e : treap.entrySet()) {
            result.add(e);
        }

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

    @Test
    void firstAndLastEntry() {
        TreapMap<Integer, String> treap = new TreapMap<>();
        treap.put(20, "B");
        treap.put(10, "A");
        treap.put(30, "C");

        assertEquals(10, treap.firstEntry().getKey());
        assertEquals(30, treap.lastEntry().getKey());
    }

    @Test
    void ceilingEntry() {
        TreapMap<Integer, String> treap = new TreapMap<>();
        treap.put(10, "A");
        treap.put(20, "B");
        treap.put(30, "C");

        assertEquals(20, treap.ceilingEntry(15).getKey());
        assertEquals(10, treap.ceilingEntry(10).getKey());
        assertNull(treap.ceilingEntry(40));
    }

    @Test
    void floorEntry() {
        TreapMap<Integer, String> treap = new TreapMap<>();
        treap.put(10, "A");
        treap.put(20, "B");
        treap.put(30, "C");

        assertEquals(10, treap.floorEntry(15).getKey());
        assertEquals(30, treap.floorEntry(30).getKey());
        assertNull(treap.floorEntry(5));
    }

    @Test
    void lowerEntry() {
        TreapMap<Integer, String> treap = new TreapMap<>();
        treap.put(10, "A");
        treap.put(20, "B");
        treap.put(30, "C");

        assertEquals(10, treap.lowerEntry(20).getKey());
        assertNull(treap.lowerEntry(10));
    }

    @Test
    void higherEntry() {
        TreapMap<Integer, String> treap = new TreapMap<>();
        treap.put(10, "A");
        treap.put(20, "B");
        treap.put(30, "C");

        assertEquals(30, treap.higherEntry(20).getKey());
        assertNull(treap.higherEntry(30));
    }

    @Test
    void subMapRange() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        for (int i = 0; i < 10; i++) {
            treap.put(i, "V" + i);
        }

        var result = new java.util.ArrayList<Entry<Integer, String>>();
        for (var e : treap.subMap(3, 7)) {
            result.add(e);
        }

        assertEquals(4, result.size());
        assertEquals(3, result.get(0).getKey());
        assertEquals(6, result.get(3).getKey());
    }

    @Test
    void nullKeyThrows() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        assertThrows(IllegalArgumentException.class, () -> treap.put(null, "A"));
        assertThrows(IllegalArgumentException.class, () -> treap.get(null));
        assertThrows(IllegalArgumentException.class, () -> treap.remove(null));
    }
    @Test
    void emptyTreeSortedMethods() {
        TreapMap<Integer, String> treap = new TreapMap<>();

        assertNull(treap.firstEntry());
        assertNull(treap.lastEntry());
        assertNull(treap.ceilingEntry(10));
        assertNull(treap.floorEntry(10));
    }

}