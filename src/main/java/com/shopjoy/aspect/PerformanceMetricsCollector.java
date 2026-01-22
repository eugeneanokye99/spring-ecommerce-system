package com.shopjoy.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class PerformanceMetricsCollector {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceMetricsCollector.class);
    
    private final Map<String, List<Long>> metrics = new ConcurrentHashMap<>();
    private final Map<String, Long> callCounts = new ConcurrentHashMap<>();
    
    public void recordMetric(String category, String methodKey, long executionTime) {
        String key = category + ":" + methodKey;
        
        metrics.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>()))
               .add(executionTime);
        
        callCounts.merge(key, 1L, (oldValue, newValue) -> oldValue + newValue);
    }
    
    public Map<String, Object> getMetricsForMethod(String category, String methodKey) {
        String key = category + ":" + methodKey;
        List<Long> times = metrics.get(key);
        
        if (times == null || times.isEmpty()) {
            return Collections.emptyMap();
        }
        
        return calculateStatistics(times, callCounts.get(key));
    }
    
    public Map<String, Map<String, Object>> getAllMetrics() {
        Map<String, Map<String, Object>> allMetrics = new LinkedHashMap<>();
        
        for (Map.Entry<String, List<Long>> entry : metrics.entrySet()) {
            String key = entry.getKey();
            List<Long> times = entry.getValue();
            Long count = callCounts.get(key);
            
            allMetrics.put(key, calculateStatistics(times, count));
        }
        
        return allMetrics;
    }
    
    public Map<String, Map<String, Object>> getMetricsByCategory(String category) {
        Map<String, Map<String, Object>> categoryMetrics = new LinkedHashMap<>();
        
        for (Map.Entry<String, List<Long>> entry : metrics.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(category + ":")) {
                List<Long> times = entry.getValue();
                Long count = callCounts.get(key);
                categoryMetrics.put(key, calculateStatistics(times, count));
            }
        }
        
        return categoryMetrics;
    }
    
    public List<Map<String, Object>> getSlowestMethods(int limit) {
        return metrics.entrySet().stream()
            .map(entry -> {
                String key = entry.getKey();
                List<Long> times = entry.getValue();
                Long count = callCounts.get(key);
                Map<String, Object> stats = calculateStatistics(times, count);
                stats.put("methodKey", key);
                return stats;
            })
            .sorted((m1, m2) -> {
                Long avg1 = (Long) m1.get("average");
                Long avg2 = (Long) m2.get("average");
                return avg2.compareTo(avg1);
            })
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getMostCalledMethods(int limit) {
        return callCounts.entrySet().stream()
            .map(entry -> {
                Map<String, Object> info = new HashMap<>();
                info.put("methodKey", entry.getKey());
                info.put("callCount", entry.getValue());
                
                List<Long> times = metrics.get(entry.getKey());
                if (times != null && !times.isEmpty()) {
                    long avg = times.stream().mapToLong(Long::longValue).sum() / times.size();
                    info.put("averageTime", avg);
                }
                
                return info;
            })
            .sorted((m1, m2) -> {
                Long count1 = (Long) m1.get("callCount");
                Long count2 = (Long) m2.get("callCount");
                return count2.compareTo(count1);
            })
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    private Map<String, Object> calculateStatistics(List<Long> times, Long callCount) {
        Map<String, Object> stats = new LinkedHashMap<>();
        
        if (times.isEmpty()) {
            return stats;
        }
        
        List<Long> sortedTimes = new ArrayList<>(times);
        Collections.sort(sortedTimes);
        
        long min = sortedTimes.get(0);
        long max = sortedTimes.get(sortedTimes.size() - 1);
        long sum = sortedTimes.stream().mapToLong(Long::longValue).sum();
        long average = sum / sortedTimes.size();
        long median = calculateMedian(sortedTimes);
        long p95 = calculatePercentile(sortedTimes, 95);
        long p99 = calculatePercentile(sortedTimes, 99);
        
        stats.put("callCount", callCount);
        stats.put("min", min);
        stats.put("max", max);
        stats.put("average", average);
        stats.put("median", median);
        stats.put("p95", p95);
        stats.put("p99", p99);
        stats.put("totalTime", sum);
        
        return stats;
    }
    
    private long calculateMedian(List<Long> sortedTimes) {
        int size = sortedTimes.size();
        if (size % 2 == 0) {
            return (sortedTimes.get(size / 2 - 1) + sortedTimes.get(size / 2)) / 2;
        } else {
            return sortedTimes.get(size / 2);
        }
    }
    
    private long calculatePercentile(List<Long> sortedTimes, int percentile) {
        int index = (int) Math.ceil(percentile / 100.0 * sortedTimes.size()) - 1;
        index = Math.max(0, Math.min(index, sortedTimes.size() - 1));
        return sortedTimes.get(index);
    }
    
    public void clearMetrics() {
        metrics.clear();
        callCounts.clear();
        logger.info("Performance metrics cleared");
    }
    
    public void printMetricsReport() {
        logger.info("===== PERFORMANCE METRICS REPORT =====");
        
        for (String category : Arrays.asList("service", "database", "api", "graphql")) {
            Map<String, Map<String, Object>> categoryMetrics = getMetricsByCategory(category);
            
            if (!categoryMetrics.isEmpty()) {
                logger.info("Category: {}", category.toUpperCase());
                
                for (Map.Entry<String, Map<String, Object>> entry : categoryMetrics.entrySet()) {
                    String methodKey = entry.getKey().substring(category.length() + 1);
                    Map<String, Object> stats = entry.getValue();
                    
                    logger.info("  Method: {}", methodKey);
                    logger.info("    Calls: {}, Avg: {}ms, Min: {}ms, Max: {}ms, Median: {}ms, P95: {}ms, P99: {}ms",
                        stats.get("callCount"),
                        stats.get("average"),
                        stats.get("min"),
                        stats.get("max"),
                        stats.get("median"),
                        stats.get("p95"),
                        stats.get("p99")
                    );
                }
            }
        }
        
        logger.info("===== TOP 10 SLOWEST METHODS =====");
        List<Map<String, Object>> slowest = getSlowestMethods(10);
        for (Map<String, Object> method : slowest) {
            logger.info("  {}: Avg={}ms, Max={}ms, Calls={}",
                method.get("methodKey"),
                method.get("average"),
                method.get("max"),
                method.get("callCount")
            );
        }
        
        logger.info("===== TOP 10 MOST CALLED METHODS =====");
        List<Map<String, Object>> mostCalled = getMostCalledMethods(10);
        for (Map<String, Object> method : mostCalled) {
            logger.info("  {}: Calls={}, Avg={}ms",
                method.get("methodKey"),
                method.get("callCount"),
                method.get("averageTime")
            );
        }
        
        logger.info("======================================");
    }
}
