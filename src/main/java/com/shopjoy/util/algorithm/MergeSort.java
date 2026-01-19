package com.shopjoy.util.algorithm;

import com.shopjoy.entity.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {
    
    public static void sort(List<Product> products, Comparator<Product> comparator) {
        if (products == null || products.size() <= 1) {
            return;
        }
        List<Product> temp = new ArrayList<>(products);
        mergeSort(products, temp, 0, products.size() - 1, comparator);
    }
    
    private static void mergeSort(List<Product> products, List<Product> temp, int left, int right, Comparator<Product> comparator) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(products, temp, left, mid, comparator);
            mergeSort(products, temp, mid + 1, right, comparator);
            merge(products, temp, left, mid, right, comparator);
        }
    }
    
    private static void merge(List<Product> products, List<Product> temp, int left, int mid, int right, Comparator<Product> comparator) {
        for (int i = left; i <= right; i++) {
            temp.set(i, products.get(i));
        }
        
        int i = left;
        int j = mid + 1;
        int k = left;
        
        while (i <= mid && j <= right) {
            if (comparator.compare(temp.get(i), temp.get(j)) <= 0) {
                products.set(k++, temp.get(i++));
            } else {
                products.set(k++, temp.get(j++));
            }
        }
        
        while (i <= mid) {
            products.set(k++, temp.get(i++));
        }
    }
}
