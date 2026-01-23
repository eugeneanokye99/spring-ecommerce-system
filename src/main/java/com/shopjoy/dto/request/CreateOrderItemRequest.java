package com.shopjoy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Request for adding an item to a new order")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemRequest {

    @Schema(description = "Product ID to order", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Integer productId;

    @Schema(description = "Quantity of the product to order", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @Schema(description = "Price of the product at the time of order", example = "1299.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
}
