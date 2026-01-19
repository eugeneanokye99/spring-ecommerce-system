package com.shopjoy.util;

import java.util.Set;

/**
 * The type Sort validator.
 */
public class SortValidator {
    
    private static final Set<String> VALID_PRODUCT_SORT_FIELDS = Set.of(
        "product_id", "product_name", "price", "cost_price", 
        "created_at", "updated_at", "category_id"
    );
    
    private static final Set<String> VALID_DIRECTIONS = Set.of("ASC", "DESC");

    /**
     * Is valid product sort field boolean.
     *
     * @param field the field
     * @return the boolean
     */
    public static boolean isValidProductSortField(String field) {
        return field != null && VALID_PRODUCT_SORT_FIELDS.contains(field.toLowerCase());
    }

    /**
     * Is valid direction boolean.
     *
     * @param direction the direction
     * @return the boolean
     */
    public static boolean isValidDirection(String direction) {
        return direction != null && VALID_DIRECTIONS.contains(direction.toUpperCase());
    }

    /**
     * Gets safe product sort field.
     *
     * @param field the field
     * @return the safe product sort field
     */
    public static String getSafeProductSortField(String field) {
        if (!isValidProductSortField(field)) {
            return "product_id";
        }
        return field.toLowerCase();
    }

    /**
     * Gets safe direction.
     *
     * @param direction the direction
     * @return the safe direction
     */
    public static String getSafeDirection(String direction) {
        if (!isValidDirection(direction)) {
            return "ASC";
        }
        return direction.toUpperCase();
    }
}
