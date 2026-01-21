package com.shopjoy.graphql.resolver.mutation;

import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.service.InventoryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class InventoryMutationResolver {

    private final InventoryService inventoryService;

    public InventoryMutationResolver(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @MutationMapping
    public InventoryResponse updateStock(@Argument Long productId, @Argument Integer quantity) {
        return inventoryService.updateStock(productId.intValue(), quantity);
    }

    @MutationMapping
    public InventoryResponse reserveStock(@Argument Long productId, @Argument Integer quantity) {
        inventoryService.reserveStock(productId.intValue(), quantity);
        return inventoryService.getInventoryByProduct(productId.intValue());
    }

    @MutationMapping
    public InventoryResponse releaseStock(@Argument Long productId, @Argument Integer quantity) {
        inventoryService.releaseStock(productId.intValue(), quantity);
        return inventoryService.getInventoryByProduct(productId.intValue());
    }
}
