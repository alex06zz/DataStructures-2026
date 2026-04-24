package test.java.sorting;

import main.java.sorting.SortingAlgorithms;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SortingAlgorithmsTest {

    private List<Integer> sampleData() {
        return Arrays.asList(5, 2, 9, 1, 5, 6);
    }

    private List<Integer> sortedData() {
        return Arrays.asList(1, 2, 5, 5, 6, 9);
    }

    @Test
    void treapSort() {
        List<Integer> result = SortingAlgorithms.treapSort(sampleData());
        assertEquals(sortedData(), result);
    }

    @Test
    void pqSort() {
        List<Integer> result = SortingAlgorithms.pqSort(sampleData());
        assertEquals(sortedData(), result);
    }

    @Test
    void javaSort() {
        List<Integer> result = SortingAlgorithms.javaSort(sampleData());
        assertEquals(sortedData(), result);
    }

    @Test
    void mergeSort() {
        List<Integer> result = SortingAlgorithms.mergeSort(sampleData());
        assertEquals(sortedData(), result);
    }

    @Test
    void quickSort() {
        List<Integer> result = SortingAlgorithms.quickSort(sampleData());
        assertEquals(sortedData(), result);
    }

    @Test
    void emptyList() {
        List<Integer> empty = new ArrayList<>();

        assertEquals(empty, SortingAlgorithms.treapSort(empty));
        assertEquals(empty, SortingAlgorithms.pqSort(empty));
        assertEquals(empty, SortingAlgorithms.javaSort(empty));
        assertEquals(empty, SortingAlgorithms.mergeSort(empty));
        assertEquals(empty, SortingAlgorithms.quickSort(empty));
    }

    @Test
    void singleElement() {
        List<Integer> single = Collections.singletonList(42);

        assertEquals(single, SortingAlgorithms.treapSort(single));
        assertEquals(single, SortingAlgorithms.pqSort(single));
        assertEquals(single, SortingAlgorithms.javaSort(single));
        assertEquals(single, SortingAlgorithms.mergeSort(single));
        assertEquals(single, SortingAlgorithms.quickSort(single));
    }

    @Test
    void alreadySorted() {
        List<Integer> sorted = Arrays.asList(1, 2, 3, 4, 5);

        assertEquals(sorted, SortingAlgorithms.treapSort(sorted));
        assertEquals(sorted, SortingAlgorithms.pqSort(sorted));
        assertEquals(sorted, SortingAlgorithms.javaSort(sorted));
        assertEquals(sorted, SortingAlgorithms.mergeSort(sorted));
        assertEquals(sorted, SortingAlgorithms.quickSort(sorted));
    }

    @Test
    void reverseSorted() {
        List<Integer> reversed = Arrays.asList(5, 4, 3, 2, 1);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);

        assertEquals(expected, SortingAlgorithms.treapSort(reversed));
        assertEquals(expected, SortingAlgorithms.pqSort(reversed));
        assertEquals(expected, SortingAlgorithms.javaSort(reversed));
        assertEquals(expected, SortingAlgorithms.mergeSort(reversed));
        assertEquals(expected, SortingAlgorithms.quickSort(reversed));
    }

    @Test
    void randomLargeInput() {
        List<Integer> data = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            data.add(rand.nextInt(10000));
        }

        List<Integer> expected = new ArrayList<>(data);
        Collections.sort(expected);

        assertEquals(expected, SortingAlgorithms.treapSort(data));
        assertEquals(expected, SortingAlgorithms.pqSort(data));
        assertEquals(expected, SortingAlgorithms.javaSort(data));
        assertEquals(expected, SortingAlgorithms.mergeSort(data));
        assertEquals(expected, SortingAlgorithms.quickSort(data));
    }

    @Test
    void negativeNumbers() {
        List<Integer> data = Arrays.asList(-5, -1, -3, 2, 0, -5);
        List<Integer> expected = Arrays.asList(-5, -5, -3, -1, 0, 2);

        assertEquals(expected, SortingAlgorithms.treapSort(data));
        assertEquals(expected, SortingAlgorithms.pqSort(data));
        assertEquals(expected, SortingAlgorithms.javaSort(data));
        assertEquals(expected, SortingAlgorithms.mergeSort(data));
        assertEquals(expected, SortingAlgorithms.quickSort(data));
    }
}