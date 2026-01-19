package com.shopjoy.util;

import com.shopjoy.entity.Product;
import com.shopjoy.util.algorithm.BinarySearch;
import com.shopjoy.util.algorithm.MergeSort;
import com.shopjoy.util.algorithm.QuickSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PerformanceComparison {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceComparison.class);

    public record SortingResult(String algorithm, long executionTimeMs, int dataSize) {

        @Override
            public String toString() {
                return String.format("%s: %d ms (dataset size: %d)", algorithm, executionTimeMs, dataSize);
            }
        }

    public record SearchResult(String algorithm, long executionTimeMs, boolean found, int dataSize) {

        @Override
            public String toString() {
                return String.format("%s: %d ms (found: %s, dataset size: %d)",
                        algorithm, executionTimeMs, found, dataSize);
            }
        }
    
    public static List<SortingResult> compareSortingAlgorithms(List<Product> products, String sortField) {
        List<SortingResult> results = new ArrayList<>();
        Comparator<Product> comparator = getComparator(sortField);
        
        logger.info("Starting sorting performance comparison for {} products", products.size());
        
        List<Product> quickSortData = new ArrayList<>(products);
        long startTime = System.currentTimeMillis();
        QuickSort.sort(quickSortData, comparator);
        long quickSortTime = System.currentTimeMillis() - startTime;
        results.add(new SortingResult("QuickSort", quickSortTime, products.size()));
        logger.info("QuickSort completed in {} ms", quickSortTime);
        
        List<Product> mergeSortData = new ArrayList<>(products);
        startTime = System.currentTimeMillis();
        MergeSort.sort(mergeSortData, comparator);
        long mergeSortTime = System.currentTimeMillis() - startTime;
        results.add(new SortingResult("MergeSort", mergeSortTime, products.size()));
        logger.info("MergeSort completed in {} ms", mergeSortTime);
        
        return results;
    }
    
    public static SearchResult compareLinearSearch(List<Product> products, Integer targetId) {
        logger.info("Starting linear search performance comparison for {} products", products.size());
        
        long startTime = System.currentTimeMillis();
        boolean found = false;
        for (Product product : products) {
            if (product.getProductId() == targetId) {
                found = true;
                break;
            }
        }
        long linearSearchTime = System.currentTimeMillis() - startTime;
        
        SearchResult result = new SearchResult("Linear Search", linearSearchTime, found, products.size());
        logger.info("Linear search completed in {} ms (found: {})", linearSearchTime, found);
        
        return result;
    }
    
    public static SearchResult compareBinarySearch(List<Product> products, Integer targetId) {
        logger.info("Starting binary search performance comparison for {} products", products.size());
        
        List<Product> sortedProducts = new ArrayList<>(products);
        QuickSort.sort(sortedProducts, Comparator.comparing(Product::getProductId));
        
        long startTime = System.currentTimeMillis();
        int index = BinarySearch.searchByProductId(sortedProducts, targetId);
        boolean found = index != -1;
        long binarySearchTime = System.currentTimeMillis() - startTime;
        
        SearchResult result = new SearchResult("Binary Search", binarySearchTime, found, products.size());
        logger.info("Binary search completed in {} ms (found: {})", binarySearchTime, found);
        
        return result;
    }
    
    public static String generatePerformanceReport(List<SortingResult> sortingResults, 
                                                     SearchResult linearSearch, 
                                                     SearchResult binarySearch) {
        StringBuilder report = new StringBuilder();
        report.append("\n");
        report.append("========================================\n");
        report.append("PERFORMANCE COMPARISON REPORT\n");
        report.append("========================================\n\n");
        
        report.append("SORTING ALGORITHMS:\n");
        report.append("----------------------------------------\n");
        for (SortingResult result : sortingResults) {
            report.append(result.toString()).append("\n");
        }
        
        if (sortingResults.size() >= 2) {
            SortingResult quickSort = sortingResults.get(0);
            SortingResult mergeSort = sortingResults.get(1);
            
            if (quickSort.executionTimeMs() < mergeSort.executionTimeMs()) {
                double improvement = ((double) (mergeSort.executionTimeMs() - quickSort.executionTimeMs())
                        / mergeSort.executionTimeMs()) * 100;
                report.append(String.format("QuickSort is %.2f%% faster than MergeSort\n", improvement));
            } else if (mergeSort.executionTimeMs() < quickSort.executionTimeMs()) {
                double improvement = ((double) (quickSort.executionTimeMs() - mergeSort.executionTimeMs())
                        / quickSort.executionTimeMs()) * 100;
                report.append(String.format("MergeSort is %.2f%% faster than QuickSort\n", improvement));
            } else {
                report.append("Both algorithms performed equally\n");
            }
        }
        
        report.append("\nSEARCH ALGORITHMS:\n");
        report.append("----------------------------------------\n");
        if (linearSearch != null) {
            report.append(linearSearch.toString()).append("\n");
        }
        if (binarySearch != null) {
            report.append(binarySearch.toString()).append("\n");
        }
        
        if (linearSearch != null && binarySearch != null) {
            if (binarySearch.executionTimeMs() < linearSearch.executionTimeMs()) {
                double improvement = ((double) (linearSearch.executionTimeMs() - binarySearch.executionTimeMs())
                        / linearSearch.executionTimeMs()) * 100;
                report.append(String.format("Binary Search is %.2f%% faster than Linear Search\n", improvement));
            } else if (linearSearch.executionTimeMs() < binarySearch.executionTimeMs()) {
                report.append("Note: Binary Search includes sorting overhead. ");
                report.append("For sorted data, Binary Search is typically faster.\n");
            }
        }
        
        report.append("\nRECOMMENDATIONS:\n");
        report.append("----------------------------------------\n");
        report.append("- For small datasets (<100): Use database ORDER BY\n");
        report.append("- For medium datasets (100-1000): QuickSort or database ORDER BY\n");
        report.append("- For large datasets (>1000): Database ORDER BY with indexes\n");
        report.append("- For repeated searches: Use Binary Search on sorted data\n");
        report.append("- For one-time searches: Linear search acceptable for small datasets\n");
        report.append("- MergeSort: Stable sort, guaranteed O(n log n), more memory\n");
        report.append("- QuickSort: In-place sort, average O(n log n), less memory\n");
        report.append("========================================\n");
        
        return report.toString();
    }
    
    private static Comparator<Product> getComparator(String sortField) {
        return switch (sortField.toLowerCase()) {
            case "product_id" -> Comparator.comparing(Product::getProductId);
            case "product_name" -> Comparator.comparing(Product::getProductName, String.CASE_INSENSITIVE_ORDER);
            case "price" -> Comparator.comparing(Product::getPrice);
            case "cost_price" -> Comparator.comparing(Product::getCostPrice);
            case "created_at" -> Comparator.comparing(Product::getCreatedAt);
            case "updated_at" -> Comparator.comparing(Product::getUpdatedAt);
            case "category_id" -> Comparator.comparing(Product::getCategoryId);
            default -> Comparator.comparing(Product::getProductId);
        };
    }
}
