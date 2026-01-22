package com.shopjoy.performance.report;

import java.util.Map;

public class SystemPerformanceSection {
    
    private Map<String, Object> metrics;

    public Map<String, Object> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, Object> metrics) {
        this.metrics = metrics;
    }
}
