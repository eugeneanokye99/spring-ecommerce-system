package com.shopjoy.util;

import com.shopjoy.entity.Product;

import java.util.Comparator;

public class ProductComparators {

    public static final Comparator<Product> BY_PRICE_ASC = Comparator.comparing(Product::getPrice,
            Comparator.nullsLast(Comparator.naturalOrder()));

    public static final Comparator<Product> BY_PRICE_DESC = Comparator.comparing(Product::getPrice,
            Comparator.nullsLast(Comparator.reverseOrder()));

    public static final Comparator<Product> BY_NAME_ASC = Comparator.comparing(Product::getProductName,
            Comparator.nullsLast(Comparator.naturalOrder()));

    public static final Comparator<Product> BY_NAME_DESC = Comparator.comparing(Product::getProductName,
            Comparator.nullsLast(Comparator.reverseOrder()));

    public static final Comparator<Product> BY_CREATED_DATE_ASC = Comparator.comparing(Product::getCreatedAt,
            Comparator.nullsLast(Comparator.naturalOrder()));

    public static final Comparator<Product> BY_CREATED_DATE_DESC = Comparator.comparing(Product::getCreatedAt,
            Comparator.nullsLast(Comparator.reverseOrder()));

    public static final Comparator<Product> BY_ID_ASC = Comparator.comparing(Product::getProductId,
            Comparator.nullsLast(Comparator.naturalOrder()));

    public static final Comparator<Product> BY_ID_DESC = Comparator.comparing(Product::getProductId,
            Comparator.nullsLast(Comparator.reverseOrder()));

    public static Comparator<Product> getComparator(String sortBy, String direction) {
        boolean isDesc = "DESC".equalsIgnoreCase(direction);

        return switch (sortBy.toLowerCase()) {
            case "price" -> isDesc ? BY_PRICE_DESC : BY_PRICE_ASC;
            case "name", "product_name" -> isDesc ? BY_NAME_DESC : BY_NAME_ASC;
            case "createdat", "created_at" -> isDesc ? BY_CREATED_DATE_DESC : BY_CREATED_DATE_ASC;
            default -> isDesc ? BY_ID_DESC : BY_ID_ASC;
        };
    }
}
