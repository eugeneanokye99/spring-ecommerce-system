package com.shopjoy.entity;

public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

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
