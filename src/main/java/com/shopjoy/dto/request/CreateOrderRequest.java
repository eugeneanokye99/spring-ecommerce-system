package com.shopjoy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request for creating a new order with shipping and payment information")
@Setter
@Getter
@Builder
public class CreateOrderRequest {
    
    @Schema(description = "User ID placing the order", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Integer userId;
    
    @Schema(description = "Complete shipping address for order delivery", example = "123 Main St, Apt 4B, New York, NY 10001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;
    
    @Schema(description = "Additional order notes or special instructions", example = "Please call before delivery")
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

   

}
