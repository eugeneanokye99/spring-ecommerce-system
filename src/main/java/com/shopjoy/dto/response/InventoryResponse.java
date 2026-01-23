package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response DTO for Inventory.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private int inventoryId;
    private int productId;
    private String productName;
    private int stockQuantity;
    private int reorderLevel;
    private LocalDateTime lastRestocked;

}
