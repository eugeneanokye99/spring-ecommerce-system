package com.shopjoy.util;

public class OptimizationRecommendation {
    
    public static String recommendSortingAlgorithm(int datasetSize, String sortField) {
        if (datasetSize < 100) {
            return "Use database ORDER BY - overhead of in-memory sorting not worth it for small datasets";
        } else if (datasetSize < 10000) {
            return "QuickSort - best average performance O(n log n) for medium datasets";
        } else {
            return "MergeSort - stable and predictable O(n log n) for large datasets, no worst-case degradation";
        }
    }
    
    public static String recommendSearchAlgorithm(int datasetSize, boolean isSorted) {
        if (!isSorted) {
            return "Use database WHERE clause or Linear Search - unsorted data requires full scan anyway";
        } else if (datasetSize < 1000) {
            return "Linear Search - simple and fast O(n) for small datasets, low overhead";
        } else if (datasetSize < 100000) {
            return "Binary Search - logarithmic O(log n) time for large sorted datasets";
        } else {
            return "Jump Search or Interpolation Search - better cache performance for very large datasets";
        }
    }
    
    public static String recommendPaginationStrategy(boolean realTimeData, int pageSize) {
        if (realTimeData) {
            return "Cursor-based pagination - avoids missing/duplicate items in real-time data streams";
        } else if (pageSize > 100) {
            return "Cursor-based pagination - more efficient for large page sizes";
        } else {
            return "Offset-based pagination - simpler implementation, works well for static data";
        }
    }
    
    public static String recommendSortAlgorithmByDataCharacteristics(
        int datasetSize,
        boolean nearlySorted,
        boolean uniformDistribution
    ) {
        if (nearlySorted && datasetSize < 10000) {
            return "Insertion Sort - O(n) for nearly sorted data";
        } else if (datasetSize < 50) {
            return "Insertion Sort - simple and efficient for very small datasets";
        } else if (datasetSize > 100000) {
            return "MergeSort - predictable performance for large datasets, no worst case";
        } else {
            return "QuickSort - best average case performance for random data";
        }
    }
    
    public static String recommendMemoryOptimization(int datasetSize, long availableMemory) {
        long estimatedMemory = datasetSize * 100L;
        
        if (estimatedMemory > availableMemory) {
            return "Use database operations - dataset too large for in-memory processing";
        } else if (estimatedMemory > availableMemory * 0.5) {
            return "HeapSort - in-place sorting O(1) space complexity";
        } else {
            return "MergeSort - extra space for stability and predictable performance";
        }
    }
}
