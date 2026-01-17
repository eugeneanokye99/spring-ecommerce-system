package com.shopjoy.dto.request;

import com.shopjoy.entity.OrderStatus;
import com.shopjoy.entity.PaymentStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating an existing order.
 */
@Setter
@Getter
public class UpdateOrderRequest {
    
    private OrderStatus status;
    
    private PaymentStatus paymentStatus;
    
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    /**
     * Instantiates a new Update order request.
     */
    public UpdateOrderRequest() {
    }

}
