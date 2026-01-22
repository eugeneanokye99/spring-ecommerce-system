package com.shopjoy.util;

import com.shopjoy.entity.Order;

import java.util.Comparator;

public class OrderComparators {
    
    public static final Comparator<Order> BY_ORDER_DATE_ASC = 
        Comparator.comparing(Order::getOrderDate, Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<Order> BY_ORDER_DATE_DESC = 
        Comparator.comparing(Order::getOrderDate, Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static final Comparator<Order> BY_TOTAL_AMOUNT_ASC = 
        Comparator.comparing(Order::getTotalAmount, Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<Order> BY_TOTAL_AMOUNT_DESC = 
        Comparator.comparing(Order::getTotalAmount, Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static final Comparator<Order> BY_STATUS_ASC = 
        Comparator.comparing(o -> o.getStatus() != null ? o.getStatus().name() : "", Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<Order> BY_STATUS_DESC = 
        Comparator.comparing(o -> o.getStatus() != null ? o.getStatus().name() : "", Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static final Comparator<Order> BY_ID_ASC = 
        Comparator.comparing(Order::getOrderId, Comparator.nullsLast(Comparator.naturalOrder()));
    
    public static final Comparator<Order> BY_ID_DESC = 
        Comparator.comparing(Order::getOrderId, Comparator.nullsLast(Comparator.reverseOrder()));
    
    public static Comparator<Order> getComparator(String sortBy, String direction) {
        boolean isDesc = "DESC".equalsIgnoreCase(direction);
        
        switch (sortBy.toLowerCase()) {
            case "orderdate":
            case "order_date":
                return isDesc ? BY_ORDER_DATE_DESC : BY_ORDER_DATE_ASC;
            case "totalamount":
            case "total_amount":
                return isDesc ? BY_TOTAL_AMOUNT_DESC : BY_TOTAL_AMOUNT_ASC;
            case "status":
                return isDesc ? BY_STATUS_DESC : BY_STATUS_ASC;
            case "id":
            case "orderid":
                return isDesc ? BY_ID_DESC : BY_ID_ASC;
            default:
                return isDesc ? BY_ID_DESC : BY_ID_ASC;
        }
    }
}
