package com.shopjoy.performance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

class AlgorithmPerformanceTest {

    private final AlgorithmPerformanceAnalyzer analyzer = new AlgorithmPerformanceAnalyzer();

    @Test
    @DisplayName("Analyze performance with 100 items")
    void analyzeSmallDataset() {
        AlgorithmPerformanceAnalyzer.AnalysisResult result = analyzer.analyzeAllAlgorithms(100);

        assertThat(result.getDatasetSize()).isEqualTo(100);
        assertThat(result.getQuickSortResult()).isNotNull();
        assertThat(result.getMergeSortResult()).isNotNull();
        assertThat(result.getHeapSortResult()).isNotNull();
        assertThat(result.getBestSortingAlgorithm()).isNotNull();

        System.out.println("\n=== Dataset Size: 100 ===");
        printAnalysisResults(result);
    }

    @Test
    @DisplayName("Analyze performance with 1000 items")
    void analyzeMediumDataset() {
        AlgorithmPerformanceAnalyzer.AnalysisResult result = analyzer.analyzeAllAlgorithms(1000);

        assertThat(result.getDatasetSize()).isEqualTo(1000);
        assertThat(result.getBinarySearchResult()).isNotNull();
        assertThat(result.getLinearSearchResult()).isNotNull();
        assertThat(result.getBestSearchAlgorithm()).isNotNull();

        System.out.println("\n=== Dataset Size: 1000 ===");
        printAnalysisResults(result);
    }

    @Test
    @DisplayName("Analyze performance with 10000 items")
    void analyzeLargeDataset() {
        AlgorithmPerformanceAnalyzer.AnalysisResult result = analyzer.analyzeAllAlgorithms(10000);

        assertThat(result.getDatasetSize()).isEqualTo(10000);
        assertThat(result.getRecommendations()).isNotEmpty();

        System.out.println("\n=== Dataset Size: 10000 ===");
        printAnalysisResults(result);
        
        assertThat(result.getQuickSortResult().getExecutionTimeMs())
            .isLessThan(result.getMergeSortResult().getExecutionTimeMs() * 2);
    }

    @Test
    @DisplayName("Compare sorting algorithms across different sizes")
    void compareSortingAlgorithmsAcrossSizes() {
        AlgorithmPerformanceAnalyzer.AnalysisResult small = analyzer.analyzeAllAlgorithms(100);
        AlgorithmPerformanceAnalyzer.AnalysisResult medium = analyzer.analyzeAllAlgorithms(1000);
        AlgorithmPerformanceAnalyzer.AnalysisResult large = analyzer.analyzeAllAlgorithms(10000);

        System.out.println("\n=== Sorting Algorithm Comparison ===");
        System.out.println("QuickSort:");
        System.out.println("  100:   " + small.getQuickSortResult().getExecutionTimeMs() + "ms");
        System.out.println("  1000:  " + medium.getQuickSortResult().getExecutionTimeMs() + "ms");
        System.out.println("  10000: " + large.getQuickSortResult().getExecutionTimeMs() + "ms");

        System.out.println("\nMergeSort:");
        System.out.println("  100:   " + small.getMergeSortResult().getExecutionTimeMs() + "ms");
        System.out.println("  1000:  " + medium.getMergeSortResult().getExecutionTimeMs() + "ms");
        System.out.println("  10000: " + large.getMergeSortResult().getExecutionTimeMs() + "ms");

        System.out.println("\nHeapSort:");
        System.out.println("  100:   " + small.getHeapSortResult().getExecutionTimeMs() + "ms");
        System.out.println("  1000:  " + medium.getHeapSortResult().getExecutionTimeMs() + "ms");
        System.out.println("  10000: " + large.getHeapSortResult().getExecutionTimeMs() + "ms");
    }

    @Test
    @DisplayName("Compare search algorithms across different sizes")
    void compareSearchAlgorithmsAcrossSizes() {
        AlgorithmPerformanceAnalyzer.AnalysisResult small = analyzer.analyzeAllAlgorithms(100);
        AlgorithmPerformanceAnalyzer.AnalysisResult medium = analyzer.analyzeAllAlgorithms(1000);
        AlgorithmPerformanceAnalyzer.AnalysisResult large = analyzer.analyzeAllAlgorithms(10000);

        System.out.println("\n=== Search Algorithm Comparison ===");
        System.out.println("BinarySearch:");
        System.out.println("  100:   " + small.getBinarySearchResult().getExecutionTimeMs() + "ms");
        System.out.println("  1000:  " + medium.getBinarySearchResult().getExecutionTimeMs() + "ms");
        System.out.println("  10000: " + large.getBinarySearchResult().getExecutionTimeMs() + "ms");

        System.out.println("\nLinearSearch:");
        System.out.println("  100:   " + small.getLinearSearchResult().getExecutionTimeMs() + "ms");
        System.out.println("  1000:  " + medium.getLinearSearchResult().getExecutionTimeMs() + "ms");
        System.out.println("  10000: " + large.getLinearSearchResult().getExecutionTimeMs() + "ms");

        System.out.println("\nJumpSearch:");
        System.out.println("  100:   " + small.getJumpSearchResult().getExecutionTimeMs() + "ms");
        System.out.println("  1000:  " + medium.getJumpSearchResult().getExecutionTimeMs() + "ms");
        System.out.println("  10000: " + large.getJumpSearchResult().getExecutionTimeMs() + "ms");
    }

    @Test
    @DisplayName("Verify recommendations are generated")
    void verifyRecommendations() {
        AlgorithmPerformanceAnalyzer.AnalysisResult result = analyzer.analyzeAllAlgorithms(5000);

        assertThat(result.getRecommendations()).isNotEmpty();
        assertThat(result.getBestSortingAlgorithm()).isIn("QuickSort", "MergeSort", "HeapSort");
        assertThat(result.getBestSearchAlgorithm()).isIn("BinarySearch", "JumpSearch", "LinearSearch");

        System.out.println("\n=== Recommendations for 5000 items ===");
        result.getRecommendations().forEach(rec -> System.out.println("- " + rec));
    }

    private void printAnalysisResults(AlgorithmPerformanceAnalyzer.AnalysisResult result) {
        System.out.println("\nSorting Algorithms:");
        System.out.println("  QuickSort: " + result.getQuickSortResult().getFormattedOutput());
        System.out.println("  MergeSort: " + result.getMergeSortResult().getFormattedOutput());
        System.out.println("  HeapSort: " + result.getHeapSortResult().getFormattedOutput());

        System.out.println("\nSearch Algorithms:");
        System.out.println("  Binary: " + result.getBinarySearchResult().getFormattedOutput());
        System.out.println("  Linear: " + result.getLinearSearchResult().getFormattedOutput());
        System.out.println("  Jump: " + result.getJumpSearchResult().getFormattedOutput());

        System.out.println("\nBest Algorithms:");
        System.out.println("  Sorting: " + result.getBestSortingAlgorithm());
        System.out.println("  Search: " + result.getBestSearchAlgorithm());

        System.out.println("\nRecommendations:");
        result.getRecommendations().forEach(rec -> System.out.println("  - " + rec));
    }
}
