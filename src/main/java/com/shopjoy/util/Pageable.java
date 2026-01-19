package com.shopjoy.util;

import lombok.Getter;

@Getter
public class Pageable {
    
    private final int page;
    private final int size;
    private final int offset;
    
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;
    
    public Pageable(Integer page, Integer size) {
        this.page = page != null && page >= 0 ? page : DEFAULT_PAGE;
        this.size = calculateSize(size);
        this.offset = this.page * this.size;
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
}
