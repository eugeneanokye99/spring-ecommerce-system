package com.shopjoy.util;

import lombok.Getter;

import java.util.List;

@Getter
public class Page<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    public Page(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.first = pageNumber == 0;
        this.last = (pageNumber + 1) >= totalPages;
    }

    public Page(List<T> content, Pageable pageable, long totalElements) {
        this(content, pageable.getPage(), pageable.getSize(), totalElements);
    }

    public boolean hasNext() {
        return !last;
    }

    public boolean hasPrevious() {
        return !first;
    }
}
