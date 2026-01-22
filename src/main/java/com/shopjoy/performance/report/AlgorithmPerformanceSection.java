package com.shopjoy.performance.report;

import com.shopjoy.util.BenchmarkResult;

import java.util.Map;

public class AlgorithmPerformanceSection {
    
    private int datasetSize;
    private Map<String, BenchmarkResult> sortingResults;
    private Map<String, BenchmarkResult> searchResults;
    private String bestSortingAlgorithm;
    private String bestSearchAlgorithm;

    public int getDatasetSize() {
        return datasetSize;
    }

    public void setDatasetSize(int datasetSize) {
        this.datasetSize = datasetSize;
    }

    public Map<String, BenchmarkResult> getSortingResults() {
        return sortingResults;
    }

    public void setSortingResults(Map<String, BenchmarkResult> sortingResults) {
        this.sortingResults = sortingResults;
    }

    public Map<String, BenchmarkResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(Map<String, BenchmarkResult> searchResults) {
        this.searchResults = searchResults;
    }

    public String getBestSortingAlgorithm() {
        return bestSortingAlgorithm;
    }

    public void setBestSortingAlgorithm(String bestSortingAlgorithm) {
        this.bestSortingAlgorithm = bestSortingAlgorithm;
    }

    public String getBestSearchAlgorithm() {
        return bestSearchAlgorithm;
    }

    public void setBestSearchAlgorithm(String bestSearchAlgorithm) {
        this.bestSearchAlgorithm = bestSearchAlgorithm;
    }
}
