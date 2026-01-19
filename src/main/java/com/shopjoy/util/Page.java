package com.shopjoy.util;

import lombok.Getter;

import java.util.List;

/**
 * The type Page.
 *
 * @param <T> the type parameter
 */
@Getter
public class Page<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    /**
     * Instantiates a new Page.
     *
     * @param content       the content
     * @param pageNumber    the page number
     * @param pageSize      the page size
     * @param totalElements the total elements
     */
    public Page(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.first = pageNumber == 0;
        this.last = (pageNumber + 1) >= totalPages;
    }

    /**
     * Instantiates a new Page.
     *
     * @param content       the content
     * @param pageable      the pageable
     * @param totalElements the total elements
     */
    public Page(List<T> content, Pageable pageable, long totalElements) {
        this(content, pageable.getPage(), pageable.getSize(), totalElements);
    }

    /**
     * Has next boolean.
     *
     * @return the boolean
     */
    public boolean hasNext() {
        return !last;
    }

    /**
     * Has previous boolean.
     *
     * @return the boolean
     */
    public boolean hasPrevious() {
        return !first;
    }
}
