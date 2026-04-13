package test.java.map;

import main.java.map.AVLTree;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {

    // Helper method to create a sample tree for testing
    //Gna use if for all tests here -> ->
    private AVLTree<Integer, String> createSampleTree() {
        AVLTree<Integer, String> tree = new AVLTree<>();
        tree.insert(50, "fifty");
        tree.insert(30, "thirty");
        tree.insert(70, "seventy");
        tree.insert(20, "twenty");
        tree.insert(40, "forty");
        tree.insert(60, "sixty");
        tree.insert(80, "eighty");
        return tree;
    }

    @Test
    void size() {
        // Test size on empty tree
        AVLTree<Integer, String> tree = new AVLTree<>();
        assertEquals(0, tree.size());

        // Test size after adding elements
        tree.insert(10, "ten");
        assertEquals(1, tree.size());

        tree.insert(20, "twenty");
        assertEquals(2, tree.size());

        // Test size after deleting
        tree.delete(10);
        assertEquals(1, tree.size());

        // Test size on larger tree
        AVLTree<Integer, String> sample = createSampleTree();
        assertEquals(7, sample.size());
    }

    @Test
    void isEmpty() {
        // Empty tree should return true
        AVLTree<Integer, String> tree = new AVLTree<>();
        assertTrue(tree.isEmpty());

        // After adding, should return false
        tree.insert(10, "ten");
        assertFalse(tree.isEmpty());

        // After deleting all, should return true
        tree.delete(10);
        assertTrue(tree.isEmpty());

        // Large tree should return false
        AVLTree<Integer, String> sample = createSampleTree();
        assertFalse(sample.isEmpty());
    }

    @Test
    void containsKey() {
        AVLTree<Integer, String> tree = createSampleTree();

        // Test keys that exist
        assertTrue(tree.containsKey(50));
        assertTrue(tree.containsKey(30));
        assertTrue(tree.containsKey(80));

        // Test keys that don't exist
        assertFalse(tree.containsKey(100));
        assertFalse(tree.containsKey(5));
        assertFalse(tree.containsKey(55));

        // Test on empty tree
        AVLTree<Integer, String> empty = new AVLTree<>();
        assertFalse(empty.containsKey(10));

        // Test after deletion
        tree.delete(50);
        assertFalse(tree.containsKey(50));
    }

    @Test
    void search() {
        AVLTree<Integer, String> tree = createSampleTree();

        // Test getting existing keys
        assertEquals("fifty", tree.search(50));
        assertEquals("thirty", tree.search(30));
        assertEquals("eighty", tree.search(80));

        // Test getting non-existent keys (should return null)
        assertNull(tree.search(999));
        assertNull(tree.search(-5));

        // Test after update (insert with same key)
        tree.insert(50, "FIFTY");
        assertEquals("FIFTY", tree.search(50));

        // Test on empty tree
        AVLTree<Integer, String> empty = new AVLTree<>();
        assertNull(empty.search(10));
    }

    @Test
    void insert() {
        AVLTree<Integer, String> tree = new AVLTree<>();

        // Test inserting new values
        tree.insert(10, "ten");
        assertEquals(1, tree.size());
        assertEquals("ten", tree.search(10));

        // Test updating existing value (insert with same key)
        tree.insert(10, "TEN");
        assertEquals(1, tree.size());  // size shouldn't change
        assertEquals("TEN", tree.search(10));

        // Test inserting multiple values
        tree.insert(20, "twenty");
        tree.insert(30, "thirty");
        assertEquals(3, tree.size());

        // Test that tree stays balanced after many inserts
        for (int i = 1; i <= 100; i++) {
            tree.insert(i, "value" + i);
        }
        assertTrue(tree.isAVL());  // Should remain balanced
    }

    @Test
    void delete() {
        AVLTree<Integer, String> tree = createSampleTree();
        int originalSize = tree.size();

        // Test deleting existing key
        tree.delete(50);
        assertEquals(originalSize - 1, tree.size());
        assertNull(tree.search(50));

        // Test deleting non-existent key (should do nothing)
        tree.delete(999);
        assertEquals(originalSize - 1, tree.size());

        // Test deleting leaf node
        tree.delete(20);
        assertNull(tree.search(20));

        // Test deleting node with one child
        tree.insert(55, "fifty-five");
        tree.delete(55);
        assertNull(tree.search(55));

        // Test that tree stays balanced after deletions
        for (int i = 1; i <= 50; i++) {
            tree.insert(i, "value" + i);
        }
        for (int i = 1; i <= 25; i++) {
            tree.delete(i);
        }
        assertTrue(tree.isAVL());  // Should remain balanced
    }

    @Test
    void inorder() {
        AVLTree<Integer, String> tree = createSampleTree();

        // Test inorder traversal returns sorted order
        ArrayList<AVLTree.KeyValuePair<Integer, String>> result = tree.inorder();

        // Expected order: 20, 30, 40, 50, 60, 70, 80
        assertEquals(7, result.size());
        assertEquals(20, result.get(0).getKey());
        assertEquals(30, result.get(1).getKey());
        assertEquals(40, result.get(2).getKey());
        assertEquals(50, result.get(3).getKey());
        assertEquals(60, result.get(4).getKey());
        assertEquals(70, result.get(5).getKey());
        assertEquals(80, result.get(6).getKey());

        // Test on empty tree
        AVLTree<Integer, String> empty = new AVLTree<>();
        ArrayList<AVLTree.KeyValuePair<Integer, String>> emptyResult = empty.inorder();
        assertEquals(0, emptyResult.size());

        // Test on single node
        AVLTree<Integer, String> single = new AVLTree<>();
        single.insert(42, "answer");
        ArrayList<AVLTree.KeyValuePair<Integer, String>> singleResult = single.inorder();
        assertEquals(1, singleResult.size());
        assertEquals(42, singleResult.get(0).getKey());

        // Test inorder after modifications
        tree.delete(50);
        ArrayList<AVLTree.KeyValuePair<Integer, String>> afterDelete = tree.inorder();
        assertEquals(6, afterDelete.size());
        assertEquals(20, afterDelete.get(0).getKey());
        assertEquals(30, afterDelete.get(1).getKey());
        assertEquals(40, afterDelete.get(2).getKey());
        assertEquals(60, afterDelete.get(3).getKey());
        assertEquals(70, afterDelete.get(4).getKey());
        assertEquals(80, afterDelete.get(5).getKey());
    }

    @Test
    void clear() {
        AVLTree<Integer, String> tree = createSampleTree();

        // Verify tree has elements
        assertFalse(tree.isEmpty());
        assertEquals(7, tree.size());

        // Clear the tree
        tree.clear();

        // Verify tree is empty
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertNull(tree.search(50));

        // Can still add after clear
        tree.insert(100, "one hundred");
        assertEquals(1, tree.size());
        assertEquals("one hundred", tree.search(100));

        // Clear empty tree (should do nothing)
        tree.clear();
        assertTrue(tree.isEmpty());
    }

    @Test
    void getHeight() {
        AVLTree<Integer, String> tree = new AVLTree<>();

        // Empty tree height is 0
        assertEquals(0, tree.getHeight());

        // Single node height is 1
        tree.insert(10, "ten");
        assertEquals(1, tree.getHeight());

        // Add more nodes - AVL stays balanced
        tree.insert(20, "twenty");
        assertEquals(2, tree.getHeight());

        tree.insert(5, "five");
        assertEquals(2, tree.getHeight());

        // Create a larger balanced tree
        AVLTree<Integer, String> balanced = createSampleTree();
        // Height of balanced tree with 7 nodes should be 3
        assertEquals(3, balanced.getHeight());

        // After adding more nodes, height grows logarithmically
        for (int i = 1; i <= 100; i++) {
            balanced.insert(i, "value" + i);
        }
        // Height should be about log2(107) ≈ 7
        assertTrue(balanced.getHeight() <= 8);
    }

    @Test
    void isAVL() {
        AVLTree<Integer, String> tree = new AVLTree<>();

        // Empty tree is AVL balanced
        assertTrue(tree.isAVL());

        // Single node is balanced
        tree.insert(10, "ten");
        assertTrue(tree.isAVL());

        // Adding more nodes should maintain balance
        for (int i = 1; i <= 1000; i++) {
            tree.insert(i, "value" + i);
        }
        assertTrue(tree.isAVL());

        // Deleting nodes should maintain balance
        for (int i = 1; i <= 500; i++) {
            tree.delete(i);
        }
        assertTrue(tree.isAVL());

        // Create tree with specific insert order that AVL handles
        AVLTree<Integer, String> avlTree = new AVLTree<>();
        // Insert in sorted order (worst case for BST, but AVL rebalances)
        for (int i = 1; i <= 100; i++) {
            avlTree.insert(i, "value" + i);
        }
        assertTrue(avlTree.isAVL());  // AVL should remain balanced!
    }

    @Test
    void printTree() {
        AVLTree<Integer, String> tree = createSampleTree();

        // Should not throw any exceptions
        assertDoesNotThrow(tree::printTree);

        // Empty tree
        AVLTree<Integer, String> empty = new AVLTree<>();
        assertDoesNotThrow(empty::printTree);

        // Single node
        AVLTree<Integer, String> single = new AVLTree<>();
        single.insert(42, "answer");
        assertDoesNotThrow(single::printTree);
    }

    @Test
    void printInOrder() {
        AVLTree<Integer, String> tree = createSampleTree();

        // Should not throw any exceptions
        assertDoesNotThrow(tree::printInOrder);

        // Empty tree
        AVLTree<Integer, String> empty = new AVLTree<>();
        assertDoesNotThrow(empty::printInOrder);

        // Verify output matches inorder traversal (visual check)
        ArrayList<AVLTree.KeyValuePair<Integer, String>> expected = tree.inorder();
        assertEquals(7, expected.size());
    }
}