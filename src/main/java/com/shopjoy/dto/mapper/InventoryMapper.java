package com.shopjoy.dto.mapper;

import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.entity.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Inventory entity and DTOs.
 */
public class InventoryMapper {
    
    public static InventoryResponse toInventoryResponse(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        
        // Product name will be populated by service layer
        return new InventoryResponse(
            inventory.getInventoryId(),
            inventory.getProductId(),
            null, // productName - set by service
            inventory.getQuantityInStock(),
            inventory.getReorderLevel(),
            inventory.getLastRestocked()
        );
    }
    
    public static List<InventoryResponse> toInventoryResponseList(List<Inventory> inventories) {
        if (inventories == null) {
            return null;
        }
        
        List<InventoryResponse> responses = new ArrayList<>();
        for (Inventory inventory : inventories) {
            responses.add(toInventoryResponse(inventory));
        }
        return responses;
    }
}
