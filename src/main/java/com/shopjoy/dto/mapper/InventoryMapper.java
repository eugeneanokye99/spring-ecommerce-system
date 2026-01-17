package com.shopjoy.dto.mapper;

import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.entity.Inventory;


/**
 * Mapper for Inventory entity and DTOs.
 */
public class InventoryMapper {
    
    public static InventoryResponse toInventoryResponse(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        
        return new InventoryResponse(
            inventory.getInventoryId(),
            inventory.getProductId(),
            null,
            inventory.getQuantityInStock(),
            inventory.getReorderLevel(),
            inventory.getLastRestocked()
        );
    }

}
