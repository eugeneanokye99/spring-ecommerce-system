package com.shopjoy.entity;

import lombok.Getter;

/**
 * The enum Order status.
 */
@Getter
public enum OrderStatus {
    /**
     * Pending order status.
     */
    PENDING("Pending"),
    /**
     * Processing order status.
     */
    PROCESSING("Processing"),
    /**
     * Shipped order status.
     */
    SHIPPED("Shipped"),
    /**
     * Delivered order status.
     */
    DELIVERED("Delivered"),
    /**
     * Cancelled order status.
     */
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * From string order status.
     *
     * @param value the value
     * @return the order status
     */
    public static OrderStatus fromString(String value) {
        if (value == null)
            return null;
        String v = value.trim();
        try {
            return OrderStatus.valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (OrderStatus s : values()) {
                if (s.displayName.equalsIgnoreCase(v))
                    return s;
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return name();
    }
}
