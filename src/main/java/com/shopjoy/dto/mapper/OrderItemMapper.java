package com.shopjoy.dto.mapper;

import com.shopjoy.dto.response.OrderItemResponse;
import com.shopjoy.entity.OrderItem;

/**
 * Mapper for OrderItem entity and Response DTO.
 */
public class OrderItemMapper {

    /**
     * To order item response.
     *
     * @param item        the item
     * @param productName the product name
     * @return the order item response
     */
    public static OrderItemResponse toOrderItemResponse(OrderItem item, String productName) {
        if (item == null) {
            return null;
        }

        return OrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProductId())
                .productName(productName)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
