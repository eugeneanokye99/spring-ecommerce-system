package com.shopjoy.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Pagination request parameters")
@Getter
public class Pageable {
    
    @Schema(description = "Page number (0-indexed)", example = "0")
    private final int page;
    
    @Schema(description = "Number of items per page", example = "10")
    private final int size;
    
    @Schema(description = "Number of items to skip", example = "0")
    private final int offset;
    
    @Schema(description = "Sort field", example = "id")
    private final String sortBy;
    
    @Schema(description = "Sort direction", example = "ASC")
    private final String sortDirection;
    
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    public Pageable(Integer page, Integer size) {
        this(page, size, "id", "ASC");
    }
    
    public Pageable(Integer page, Integer size, String sortBy, String sortDirection) {
        this.page = page != null && page >= 0 ? page : DEFAULT_PAGE;
        this.size = calculateSize(size);
        this.offset = this.page * this.size;
        this.sortBy = sortBy != null ? sortBy : "id";
        this.sortDirection = sortDirection != null ? sortDirection : "ASC";
    }
    
    private int calculateSize(Integer size) {
        if (size == null || size <= 0) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }

    public static Pageable of(Integer page, Integer size) {
        return new Pageable(page, size);
    }
    
    public static Pageable of(Integer page, Integer size, String sortBy, String sortDirection) {
        return new Pageable(page, size, sortBy, sortDirection);
    }
}
