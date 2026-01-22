package com.shopjoy.performance.report;

import java.util.Map;

public class ApiPerformanceSection {
    
    private Map<String, Double> restMetrics;
    private Map<String, Double> graphqlMetrics;
    private String winner;
    private double performanceGain;

    public Map<String, Double> getRestMetrics() {
        return restMetrics;
    }

    public void setRestMetrics(Map<String, Double> restMetrics) {
        this.restMetrics = restMetrics;
    }

    public Map<String, Double> getGraphqlMetrics() {
        return graphqlMetrics;
    }

    public void setGraphqlMetrics(Map<String, Double> graphqlMetrics) {
        this.graphqlMetrics = graphqlMetrics;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public double getPerformanceGain() {
        return performanceGain;
    }

    public void setPerformanceGain(double performanceGain) {
        this.performanceGain = performanceGain;
    }
}
