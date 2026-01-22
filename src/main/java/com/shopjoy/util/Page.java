package com.shopjoy.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(description = "Paginated response wrapper containing page content and metadata")
@Getter
public class Page<T> {

    @Schema(description = "List of items in the current page")
    private final List<T> content;
    
    @Schema(description = "Current page number (0-indexed)", example = "0")
    private final int pageNumber;
    
    @Schema(description = "Number of items per page", example = "10")
    private final int pageSize;
    
    @Schema(description = "Total number of items across all pages", example = "100")
    private final long totalElements;
    
    @Schema(description = "Total number of pages", example = "10")
    private final int totalPages;
    
    @Schema(description = "Whether this is the first page", example = "true")
    private final boolean first;
    
    @Schema(description = "Whether this is the last page", example = "false")
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

}
