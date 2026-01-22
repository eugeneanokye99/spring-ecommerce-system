package com.shopjoy.performance;

import com.shopjoy.entity.Product;
import com.shopjoy.util.algorithm.AlgorithmBenchmark;
import com.shopjoy.util.algorithm.BenchmarkResult;
import com.shopjoy.util.algorithm.SearchAlgorithms;
import com.shopjoy.util.algorithm.SortingAlgorithms;
import com.shopjoy.util.comparator.ProductComparators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AlgorithmPerformanceAnalyzer {

    private static final Random random = new Random(42);

    public AnalysisResult analyzeAllAlgorithms(int datasetSize) {
        List<Product> dataset = generateTestData(datasetSize);
        
        AnalysisResult result = new AnalysisResult();
        result.setDatasetSize(datasetSize);
        
        result.setQuickSortResult(analyzeQuickSort(dataset));
        result.setMergeSortResult(analyzeMergeSort(dataset));
        result.setHeapSortResult(analyzeHeapSort(dataset));
        
        result.setBinarySearchResult(analyzeBinarySearch(dataset));
        result.setLinearSearchResult(analyzeLinearSearch(dataset));
        result.setJumpSearchResult(analyzeJumpSearch(dataset));
        
        result.calculateRecommendations();
        
        return result;
    }

    private BenchmarkResult analyzeQuickSort(List<Product> dataset) {
        List<Product> copy = new ArrayList<>(dataset);
        Comparator<Product> comparator = ProductComparators.BY_PRICE_ASC;
        
        return AlgorithmBenchmark.benchmarkSort(
            copy,
            list -> SortingAlgorithms.quickSort(list, comparator),
            "QuickSort"
        );
    }

    private BenchmarkResult analyzeMergeSort(List<Product> dataset) {
        List<Product> copy = new ArrayList<>(dataset);
        Comparator<Product> comparator = ProductComparators.BY_PRICE_ASC;
        
        return AlgorithmBenchmark.benchmarkSort(
            copy,
            list -> SortingAlgorithms.mergeSort(list, comparator),
            "MergeSort"
        );
    }

    private BenchmarkResult analyzeHeapSort(List<Product> dataset) {
        List<Product> copy = new ArrayList<>(dataset);
        Comparator<Product> comparator = ProductComparators.BY_PRICE_ASC;
        
        return AlgorithmBenchmark.benchmarkSort(
            copy,
            list -> SortingAlgorithms.heapSort(list, comparator),
            "HeapSort"
        );
    }

    private BenchmarkResult analyzeBinarySearch(List<Product> dataset) {
        List<Product> sorted = new ArrayList<>(dataset);
        SortingAlgorithms.quickSort(sorted, ProductComparators.BY_PRICE_ASC);
        
        BigDecimal target = dataset.get(dataset.size() / 2).getPrice();
        
        return AlgorithmBenchmark.benchmarkSearch(
            sorted,
            list -> SearchAlgorithms.binarySearch(list, target, Product::getPrice),
            "BinarySearch"
        );
    }

    private BenchmarkResult analyzeLinearSearch(List<Product> dataset) {
        BigDecimal target = dataset.get(dataset.size() / 2).getPrice();
        
        return AlgorithmBenchmark.benchmarkSearch(
            dataset,
            list -> SearchAlgorithms.linearSearch(list, p -> p.getPrice().equals(target)),
            "LinearSearch"
        );
    }

    private BenchmarkResult analyzeJumpSearch(List<Product> dataset) {
        List<Product> sorted = new ArrayList<>(dataset);
        SortingAlgorithms.quickSort(sorted, ProductComparators.BY_PRICE_ASC);
        
        BigDecimal target = dataset.get(dataset.size() / 2).getPrice();
        
        return AlgorithmBenchmark.benchmarkSearch(
            sorted,
            list -> SearchAlgorithms.jumpSearch(list, target, Product::getPrice),
            "JumpSearch"
        );
    }

    private List<Product> generateTestData(int size) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Product product = new Product();
            product.setProductId((long) i);
            product.setProductName("Product " + i);
            product.setPrice(BigDecimal.valueOf(10.0 + random.nextDouble() * 990.0));
            product.setStockQuantity(random.nextInt(1000));
            products.add(product);
        }
        return products;
    }

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
            
            double quickSortTime = quickSortResult.getExecutionTimeMs();
            double mergeSortTime = mergeSortResult.getExecutionTimeMs();
            double heapSortTime = heapSortResult.getExecutionTimeMs();
            
            if (quickSortTime <= mergeSortTime && quickSortTime <= heapSortTime) {
                bestSortingAlgorithm = "QuickSort";
                recommendations.add("QuickSort is fastest for this dataset size");
            } else if (mergeSortTime <= heapSortTime) {
                bestSortingAlgorithm = "MergeSort";
                recommendations.add("MergeSort provides stable sorting with consistent performance");
            } else {
                bestSortingAlgorithm = "HeapSort";
                recommendations.add("HeapSort is memory efficient for large datasets");
            }
            
            double binarySearchTime = binarySearchResult.getExecutionTimeMs();
            double linearSearchTime = linearSearchResult.getExecutionTimeMs();
            double jumpSearchTime = jumpSearchResult.getExecutionTimeMs();
            
            if (binarySearchTime <= jumpSearchTime && binarySearchTime <= linearSearchTime) {
                bestSearchAlgorithm = "BinarySearch";
                recommendations.add("BinarySearch is optimal for sorted data");
            } else if (jumpSearchTime <= linearSearchTime) {
                bestSearchAlgorithm = "JumpSearch";
                recommendations.add("JumpSearch balances simplicity and performance");
            } else {
                bestSearchAlgorithm = "LinearSearch";
                recommendations.add("LinearSearch is suitable for unsorted small datasets");
            }
            
            if (datasetSize < 1000) {
                recommendations.add("Consider using database sorting for datasets under 1000");
            } else if (datasetSize > 10000) {
                recommendations.add("For large datasets, consider pagination and database indexing");
            }
        }

        public int getDatasetSize() { return datasetSize; }
        public void setDatasetSize(int datasetSize) { this.datasetSize = datasetSize; }
        public BenchmarkResult getQuickSortResult() { return quickSortResult; }
        public void setQuickSortResult(BenchmarkResult quickSortResult) { this.quickSortResult = quickSortResult; }
        public BenchmarkResult getMergeSortResult() { return mergeSortResult; }
        public void setMergeSortResult(BenchmarkResult mergeSortResult) { this.mergeSortResult = mergeSortResult; }
        public BenchmarkResult getHeapSortResult() { return heapSortResult; }
        public void setHeapSortResult(BenchmarkResult heapSortResult) { this.heapSortResult = heapSortResult; }
        public BenchmarkResult getBinarySearchResult() { return binarySearchResult; }
        public void setBinarySearchResult(BenchmarkResult binarySearchResult) { this.binarySearchResult = binarySearchResult; }
        public BenchmarkResult getLinearSearchResult() { return linearSearchResult; }
        public void setLinearSearchResult(BenchmarkResult linearSearchResult) { this.linearSearchResult = linearSearchResult; }
        public BenchmarkResult getJumpSearchResult() { return jumpSearchResult; }
        public void setJumpSearchResult(BenchmarkResult jumpSearchResult) { this.jumpSearchResult = jumpSearchResult; }
        public String getBestSortingAlgorithm() { return bestSortingAlgorithm; }
        public void setBestSortingAlgorithm(String bestSortingAlgorithm) { this.bestSortingAlgorithm = bestSortingAlgorithm; }
        public String getBestSearchAlgorithm() { return bestSearchAlgorithm; }
        public void setBestSearchAlgorithm(String bestSearchAlgorithm) { this.bestSearchAlgorithm = bestSearchAlgorithm; }
        public List<String> getRecommendations() { return recommendations; }
        public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    }
}
