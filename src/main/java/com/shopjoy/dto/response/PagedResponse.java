package com.shopjoy.dto.response;

import java.util.List;

/**
 * Wrapper for paginated API responses.
 * Use this when returning lists that can be paginated.
 * 
 * Example usage:
 * PagedResponse<ProductResponse> response = new PagedResponse<>(
 *     products,      // List of products
 *     0,            // Current page (0-indexed)
 *     20,           // Page size
 *     100           // Total products in database
 * );
 */
public class PagedResponse<T> {
    
    private List<T> content;        // The actual list of items
    private int pageNumber;         // Current page (0-indexed)
    private int pageSize;           // Number of items per page
    private long totalElements;     // Total number of items across all pages
    private int totalPages;         // Total number of pages
    private boolean isFirst;        // Is this the first page?
    private boolean isLast;         // Is this the last page?
    
    // Constructor
    public PagedResponse(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        
        // Calculate total pages
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        
        // Check if first or last page
        this.isFirst = (pageNumber == 0);
        this.isLast = (pageNumber >= totalPages - 1);
    }
    
    // Getters and Setters
    public List<T> getContent() {
        return content;
    }
    
    public void setContent(List<T> content) {
        this.content = content;
    }
    
    public int getPageNumber() {
        return pageNumber;
    }
    
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public boolean isFirst() {
        return isFirst;
    }
    
    public void setFirst(boolean first) {
        isFirst = first;
    }
    
    public boolean isLast() {
        return isLast;
    }
    
    public void setLast(boolean last) {
        isLast = last;
    }
}
