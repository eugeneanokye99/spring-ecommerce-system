package com.shopjoy.graphql.resolver.query;

import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.service.InventoryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InventoryQueryResolver {

    private final InventoryService inventoryService;

    public InventoryQueryResolver(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @QueryMapping
    public InventoryResponse inventory(@Argument Long productId) {
        return inventoryService.getInventoryByProduct(productId.intValue());
    }

    @QueryMapping
    public List<InventoryResponse> lowStockProducts() {
        return inventoryService.getLowStockProducts();
    }
}
