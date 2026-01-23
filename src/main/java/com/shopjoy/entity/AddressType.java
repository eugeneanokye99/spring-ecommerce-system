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
    BILLING("Billing"),
    /**
     * Home address label.
     */
    HOME("Home"),
    /**
     * Work address label.
     */
    WORK("Work"),
    /**
     * Other address label.
     */
    OTHER("Other");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return name();
    }
}
