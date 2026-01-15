package com.shopjoy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private int inventoryId;
    private int productId;
    private int quantityInStock;
    private int reorderLevel;
    private String warehouseLocation;
    private LocalDateTime lastRestocked;
    private LocalDateTime updatedAt;
}
