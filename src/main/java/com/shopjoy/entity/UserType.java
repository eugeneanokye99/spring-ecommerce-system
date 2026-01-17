package com.shopjoy.entity;

import lombok.Getter;

/**
 * The enum User type.
 */
@Getter
public enum UserType {
    /**
     * Customer user type.
     */
    CUSTOMER("Customer"),
    /**
     * Admin user type.
     */
    ADMIN("Admin");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * From string user type.
     *
     * @param value the value
     * @return the user type
     */
    public static UserType fromString(String value) {
        if (value == null)
            return null;
        String v = value.trim();
        try {
            return UserType.valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (UserType t : values()) {
                if (t.displayName.equalsIgnoreCase(v))
                    return t;
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return name();
    }
}
