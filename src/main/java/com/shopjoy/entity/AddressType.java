package com.shopjoy.entity;

public enum AddressType {
    SHIPPING("Shipping"),
    BILLING("Billing");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return name();
    }
}
