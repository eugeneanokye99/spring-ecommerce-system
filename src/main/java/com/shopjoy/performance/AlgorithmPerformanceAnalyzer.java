
package com.shopjoy.performance;

import com.shopjoy.entity.Product;
import com.shopjoy.util.BenchmarkResult;
import com.shopjoy.util.SearchAlgorithms;
import com.shopjoy.util.SortingAlgorithms;
import com.shopjoy.util.ProductComparators;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

public class AlgorithmPerformanceAnalyzer {

    private final Random random;
    private final int warmupIterations;
    private final int timedIterations;

    public AlgorithmPerformanceAnalyzer() {
        this(System.currentTimeMillis());
    }

    public AlgorithmPerformanceAnalyzer(long seed) {
        this(seed, 5, 20);
    }

    public AlgorithmPerformanceAnalyzer(long seed, int warmupIterations, int timedIterations) {
        this.random = new Random(seed);
        this.warmupIterations = warmupIterations;
        this.timedIterations = timedIterations;
    }

    public AnalysisResult analyzeAllAlgorithms(int datasetSize) {
        List<Product> dataset = generateTestData(datasetSize);

        AnalysisResult result = new AnalysisResult();
        result.setDatasetSize(datasetSize);

        result.setQuickSortResult(benchmarkSort(dataset, "QuickSort"));
        result.setMergeSortResult(benchmarkSort(dataset, "MergeSort"));
        result.setHeapSortResult(benchmarkSort(dataset, "HeapSort"));

        // For search benchmarks, sort once and use the same sorted data for all search algorithms
        List<Product> sorted = new ArrayList<>(dataset);
        SortingAlgorithms.quickSort(sorted, ProductComparators.BY_PRICE_ASC);
        BigDecimal target = BigDecimal.valueOf(sorted.get(sorted.size() / 2).getPrice());

        result.setBinarySearchResult(benchmarkSearch(sorted, target, "BinarySearch"));
        result.setLinearSearchResult(benchmarkSearch(dataset, target, "LinearSearch"));
        result.setJumpSearchResult(benchmarkSearch(sorted, target, "JumpSearch"));

        result.calculateRecommendations();

        return result;
    }

    private BenchmarkResult benchmarkSort(List<Product> dataset, String algorithm) {
        Comparator<Product> comparator = ProductComparators.BY_PRICE_ASC;
        List<Double> times = new ArrayList<>();
        long memoryUsed = 0;
        int size = dataset.size();

        // Warmup
        for (int i = 0; i < warmupIterations; i++) {
            List<Product> copy = deepCopyProducts(dataset);
            runSort(copy, comparator, algorithm);
        }

        // Timed iterations
        for (int i = 0; i < timedIterations; i++) {
            List<Product> copy = deepCopyProducts(dataset);
            System.gc();
            try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            long startMem = getUsedMemory();
            long start = System.nanoTime();
            runSort(copy, comparator, algorithm);
            long end = System.nanoTime();
            long endMem = getUsedMemory();
            times.add((end - start) / 1_000_000.0);
            memoryUsed += Math.max(0, endMem - startMem);
        }
        double medianTime = median(times);
        long avgMemory = memoryUsed / timedIterations;
        return new BenchmarkResult(algorithm, medianTime, avgMemory, size);
    }

    private void runSort(List<Product> list, Comparator<Product> comparator, String algorithm) {
        switch (algorithm) {
            case "QuickSort" -> SortingAlgorithms.quickSort(list, comparator);
            case "MergeSort" -> SortingAlgorithms.mergeSort(list, comparator);
            case "HeapSort" -> SortingAlgorithms.heapSort(list, comparator);
            default -> throw new IllegalArgumentException("Unknown sort algorithm: " + algorithm);
        }
    }

    private BenchmarkResult benchmarkSearch(List<Product> dataset, BigDecimal target, String algorithm) {
        List<Double> times = new ArrayList<>();
        long memoryUsed = 0;
        int size = dataset.size();

        // Warmup
        for (int i = 0; i < warmupIterations; i++) {
            runSearch(dataset, target, algorithm);
        }

        // Timed iterations
        for (int i = 0; i < timedIterations; i++) {
            System.gc();
            try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            long startMem = getUsedMemory();
            long start = System.nanoTime();
            runSearch(dataset, target, algorithm);
            long end = System.nanoTime();
            long endMem = getUsedMemory();
            times.add((end - start) / 1_000_000.0);
            memoryUsed += Math.max(0, endMem - startMem);
        }
        double medianTime = median(times);
        long avgMemory = memoryUsed / timedIterations;
        return new BenchmarkResult(algorithm, medianTime, avgMemory, size);
    }

    private void runSearch(List<Product> list, BigDecimal target, String algorithm) {
        switch (algorithm) {
            case "BinarySearch" -> {
                Product key = new Product();
                key.setPrice(target.doubleValue());
                SearchAlgorithms.binarySearch(list, key, Comparator.comparing(Product::getPrice));
            }
            case "LinearSearch" -> SearchAlgorithms.linearSearch(list, p -> BigDecimal.valueOf(p.getPrice()).compareTo(target) == 0);
            case "JumpSearch" -> {
                Product key = new Product();
                key.setPrice(target.doubleValue());
                SearchAlgorithms.jumpSearch(list, key, Comparator.comparing(Product::getPrice));
            }
            default -> throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
        }
    }

    private List<Product> deepCopyProducts(List<Product> products) {
        List<Product> copy = new ArrayList<>(products.size());
        for (Product p : products) {
            Product np = new Product();
            np.setProductId(p.getProductId());
            np.setProductName(p.getProductName());
            np.setPrice(p.getPrice());
            copy.add(np);
        }
        return copy;
    }

    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static double median(List<Double> values) {
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int n = sorted.size();
        if (n % 2 == 0) {
            return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
        } else {
            return sorted.get(n / 2);
        }
    }

    private List<Product> generateTestData(int size) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Product product = new Product();
            product.setProductId(i);
            product.setProductName("Product " + i);
            // Use BigDecimal.valueOf for price
            product.setPrice(10.0 + random.nextDouble() * 990.0);
            products.add(product);
        }
        return products;
    }

    @Data
    public static class AnalysisResult {
        private int datasetSize;
        private BenchmarkResult quickSortResult;
        private BenchmarkResult mergeSortResult;
        private BenchmarkResult heapSortResult;
        private BenchmarkResult binarySearchResult;
        private BenchmarkResult linearSearchResult;
        private BenchmarkResult jumpSearchResult;
        private String bestSortingAlgorithm;
        private String bestSearchAlgorithm;
        private List<String> recommendations;

        public void calculateRecommendations() {
            this.recommendations = new ArrayList<>();
            double tolerance = 0.05; // 5% threshold

            double quickSortTime = quickSortResult.getExecutionTimeMs();
            double mergeSortTime = mergeSortResult.getExecutionTimeMs();
            double heapSortTime = heapSortResult.getExecutionTimeMs();

            // Find best sorting algorithm with tolerance
            String bestSort = null;
            if (isSignificantlyFaster(quickSortTime, mergeSortTime, tolerance) && isSignificantlyFaster(quickSortTime, heapSortTime, tolerance)) {
                bestSort = "QuickSort";
                recommendations.add("QuickSort is fastest for this dataset size");
            } else if (isSignificantlyFaster(mergeSortTime, quickSortTime, tolerance) && isSignificantlyFaster(mergeSortTime, heapSortTime, tolerance)) {
                bestSort = "MergeSort";
                recommendations.add("MergeSort provides stable sorting with consistent performance");
            } else if (isSignificantlyFaster(heapSortTime, quickSortTime, tolerance) && isSignificantlyFaster(heapSortTime, mergeSortTime, tolerance)) {
                bestSort = "HeapSort";
                recommendations.add("HeapSort is memory efficient for large datasets");
            } else {
                // Within tolerance, prefer MergeSort for stability, then HeapSort for memory
                if (mergeSortTime <= heapSortTime) {
                    bestSort = "MergeSort";
                    recommendations.add("MergeSort is preferred for stability when times are similar");
                } else {
                    bestSort = "HeapSort";
                    recommendations.add("HeapSort is preferred for memory efficiency when times are similar");
                }
            }
            bestSortingAlgorithm = bestSort;

            double binarySearchTime = binarySearchResult.getExecutionTimeMs();
            double linearSearchTime = linearSearchResult.getExecutionTimeMs();
            double jumpSearchTime = jumpSearchResult.getExecutionTimeMs();

            String bestSearch = null;
            if (isSignificantlyFaster(binarySearchTime, jumpSearchTime, tolerance) && isSignificantlyFaster(binarySearchTime, linearSearchTime, tolerance)) {
                bestSearch = "BinarySearch";
                recommendations.add("BinarySearch is optimal for sorted data");
            } else if (isSignificantlyFaster(jumpSearchTime, binarySearchTime, tolerance) && isSignificantlyFaster(jumpSearchTime, linearSearchTime, tolerance)) {
                bestSearch = "JumpSearch";
                recommendations.add("JumpSearch balances simplicity and performance");
            } else if (isSignificantlyFaster(linearSearchTime, binarySearchTime, tolerance) && isSignificantlyFaster(linearSearchTime, jumpSearchTime, tolerance)) {
                bestSearch = "LinearSearch";
                recommendations.add("LinearSearch is suitable for unsorted small datasets");
            } else {
                // Within tolerance, prefer BinarySearch for sorted, then JumpSearch
                if (binarySearchTime <= jumpSearchTime) {
                    bestSearch = "BinarySearch";
                    recommendations.add("BinarySearch is preferred for sorted data when times are similar");
                } else {
                    bestSearch = "JumpSearch";
                    recommendations.add("JumpSearch is preferred for simplicity when times are similar");
                }
            }
            bestSearchAlgorithm = bestSearch;

            if (datasetSize < 1000) {
                recommendations.add("Consider using database sorting for datasets under 1000");
            } else if (datasetSize > 10000) {
                recommendations.add("For large datasets, consider pagination and database indexing");
            }
        }

        private boolean isSignificantlyFaster(double t1, double t2, double tolerance) {
            return t1 < t2 * (1.0 - tolerance);
        }
    }
}
