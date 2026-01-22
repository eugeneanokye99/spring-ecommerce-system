package com.shopjoy.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

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
    
    public static <T> List<T> linearSearch(List<T> list, Predicate<T> predicate) {
        List<T> results = new ArrayList<>();
        
        if (list == null || list.isEmpty() || predicate == null) {
            return results;
        }
        
        for (T element : list) {
            if (predicate.test(element)) {
                results.add(element);
            }
        }
        
        return results;
    }
    
    public static <T> T linearSearchFirst(List<T> list, Predicate<T> predicate) {
        if (list == null || list.isEmpty() || predicate == null) {
            return null;
        }
        
        for (T element : list) {
            if (predicate.test(element)) {
                return element;
            }
        }
        
        return null;
    }
    
    public static <T> int jumpSearch(List<T> sortedList, T target, Comparator<T> comparator) {
        if (sortedList == null || sortedList.isEmpty() || target == null) {
            return -1;
        }
        
        int n = sortedList.size();
        int step = (int) Math.sqrt(n);
        int prev = 0;
        
        while (prev < n && comparator.compare(sortedList.get(Math.min(step, n) - 1), target) < 0) {
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
    
    public static <T> int interpolationSearch(List<T> sortedList, T target, ToDoubleFunction<T> valueExtractor) {
        if (sortedList == null || sortedList.isEmpty() || target == null) {
            return -1;
        }
        
        int low = 0;
        int high = sortedList.size() - 1;
        
        while (low <= high) {
            double targetValue = valueExtractor.applyAsDouble(target);
            double lowValue = valueExtractor.applyAsDouble(sortedList.get(low));
            double highValue = valueExtractor.applyAsDouble(sortedList.get(high));
            
            if (targetValue < lowValue || targetValue > highValue) {
                return -1;
            }
            
            if (lowValue == highValue) {
                if (lowValue == targetValue) {
                    return low;
                }
                return -1;
            }
            
            int pos = low + (int) ((targetValue - lowValue) / (highValue - lowValue) * (high - low));
            
            if (pos < low || pos > high) {
                return -1;
            }
            
            double posValue = valueExtractor.applyAsDouble(sortedList.get(pos));
            
            if (posValue == targetValue) {
                return pos;
            }
            
            if (posValue < targetValue) {
                low = pos + 1;
            } else {
                high = pos - 1;
            }
        }
        
        return -1;
    }
    
    public static <T> int exponentialSearch(List<T> sortedList, T target, Comparator<T> comparator) {
        if (sortedList == null || sortedList.isEmpty() || target == null) {
            return -1;
        }
        
        if (comparator.compare(sortedList.get(0), target) == 0) {
            return 0;
        }
        
        int i = 1;
        while (i < sortedList.size() && comparator.compare(sortedList.get(i), target) <= 0) {
            i = i * 2;
        }
        
        return binarySearchRange(sortedList, target, i / 2, Math.min(i, sortedList.size() - 1), comparator);
    }
    
    private static <T> int binarySearchRange(List<T> list, T target, int left, int right, Comparator<T> comparator) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = comparator.compare(list.get(mid), target);
            
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
}
