package com.shopjoy.entity;

import lombok.Getter;

/**
 * The enum Address type.
 */
@Getter
public enum AddressType {
    /**
     * Shipping address type.
     */
    SHIPPING("Shipping"),
    /**
     * Billing address type.
     */
    BILLING("Billing");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return name();
    }
}
