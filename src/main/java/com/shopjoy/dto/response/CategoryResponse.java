package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response DTO for Category.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    
    private int categoryId;
    private String categoryName;
    private String description;
    private Integer parentCategoryId;
    private LocalDateTime createdAt;

}
