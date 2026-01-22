package com.shopjoy.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SearchAlgorithms {
    
    public static <T> int binarySearch(List<T> sortedList, T target, Comparator<T> comparator) {
        if (sortedList == null || sortedList.isEmpty() || target == null) {
            return -1;
        }
        
        int left = 0;
        int right = sortedList.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = comparator.compare(sortedList.get(mid), target);
            
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
    
    public static <T> void linearSearch(List<T> list, Predicate<T> predicate) {
        List<T> results = new ArrayList<>();
        
        if (list == null || list.isEmpty() || predicate == null) {
            return;
        }
        
        for (T element : list) {
            if (predicate.test(element)) {
                results.add(element);
            }
        }

    }

    public static <T> int jumpSearch(List<T> sortedList, T target, Comparator<T> comparator) {
        if (sortedList == null || sortedList.isEmpty() || target == null) {
            return -1;
        }
        
        int n = sortedList.size();
        int step = (int) Math.sqrt(n);
        int prev = 0;
        
        while (comparator.compare(sortedList.get(Math.min(step, n) - 1), target) < 0) {
            prev = step;
            step += (int) Math.sqrt(n);
            if (prev >= n) {
                return -1;
            }
        }
        
        while (prev < n && comparator.compare(sortedList.get(prev), target) < 0) {
            prev++;
            if (prev == Math.min(step, n)) {
                return -1;
            }
        }
        
        if (prev < n && comparator.compare(sortedList.get(prev), target) == 0) {
            return prev;
        }
        
        return -1;
    }

}
