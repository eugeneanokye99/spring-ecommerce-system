package com.shopjoy.util.algorithm;

import com.shopjoy.entity.Product;

import java.util.Comparator;
import java.util.List;

public class BinarySearch {
    
    public static int searchByProductId(List<Product> sortedProducts, int targetId) {
        if (sortedProducts == null || sortedProducts.isEmpty()) {
            return -1;
        }
        return binarySearch(sortedProducts, 0, sortedProducts.size() - 1, targetId);
    }
    
    private static int binarySearch(List<Product> products, int left, int right, int targetId) {
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        int midId = products.get(mid).getProductId();
        
        if (midId == targetId) {
            return mid;
        } else if (midId < targetId) {
            return binarySearch(products, mid + 1, right, targetId);
        } else {
            return binarySearch(products, left, mid - 1, targetId);
        }
    }
    
    public static <T> int search(List<T> sortedList, T target, Comparator<T> comparator) {
        if (sortedList == null || sortedList.isEmpty()) {
            return -1;
        }
        return binarySearch(sortedList, 0, sortedList.size() - 1, target, comparator);
    }
    
    private static <T> int binarySearch(List<T> list, int left, int right, T target, Comparator<T> comparator) {
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        int comparison = comparator.compare(list.get(mid), target);
        
        if (comparison == 0) {
            return mid;
        } else if (comparison < 0) {
            return binarySearch(list, mid + 1, right, target, comparator);
        } else {
            return binarySearch(list, left, mid - 1, target, comparator);
        }
    }
}
