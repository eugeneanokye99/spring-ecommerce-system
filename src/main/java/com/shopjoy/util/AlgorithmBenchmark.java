package com.shopjoy.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AlgorithmBenchmark {
    
    public static <T> BenchmarkResult benchmarkSort(
        List<T> data, 
        Comparator<T> comparator,
        String algorithmName,
        SortFunction<T> sortFunction
    ) {
        List<T> dataCopy = new ArrayList<>(data);
        
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();
        
        sortFunction.sort(dataCopy, comparator);
        
        long endTime = System.nanoTime();
        long endMemory = getUsedMemory();
        
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        long memoryUsed = Math.max(0, endMemory - startMemory);
        
        return new BenchmarkResult(
            algorithmName,
            executionTimeMs,
            memoryUsed,
            data.size()
        );
    }
    
    public static <T> BenchmarkResult benchmarkSearch(
        List<T> data,
        T target,
        String algorithmName,
        SearchFunction<T> searchFunction
    ) {
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

        searchFunction.search(data, target);

        long endTime = System.nanoTime();
        long endMemory = getUsedMemory();
        
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        long memoryUsed = Math.max(0, endMemory - startMemory);
        
        return new BenchmarkResult(
            algorithmName,
            executionTimeMs,
            memoryUsed,
            data.size()
        );
    }
    
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    @FunctionalInterface
    public interface SortFunction<T> {
        void sort(List<T> list, Comparator<T> comparator);
    }
    
    @FunctionalInterface
    public interface SearchFunction<T> {
        int search(List<T> list, T target);
    }
}
