package com.shopjoy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int categoryId;
    private String categoryName;
    private String description;
    private Integer parentCategoryId;
    private LocalDateTime createdAt;
}
