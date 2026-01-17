package com.shopjoy.entity;

import lombok.Getter;

/**
 * The enum Payment status.
 */
@Getter
public enum PaymentStatus {
    /**
     * Unpaid payment status.
     */
    UNPAID("Unpaid"),
    /**
     * Paid payment status.
     */
    PAID("Paid"),
    /**
     * Refunded payment status.
     */
    REFUNDED("Refunded");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * From string payment status.
     *
     * @param value the value
     * @return the payment status
     */
    public static PaymentStatus fromString(String value) {
        if (value == null)
            return null;
        String v = value.trim();
        try {
            return PaymentStatus.valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (PaymentStatus s : values()) {
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
