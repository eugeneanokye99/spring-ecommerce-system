package com.shopjoy.performance.report;

import java.time.LocalDateTime;
import java.util.List;

public class PerformanceReport {
    
    private LocalDateTime generatedAt;
    private String reportVersion;
    private String executiveSummary;
    private AlgorithmPerformanceSection algorithmPerformance;
    private ApiPerformanceSection apiPerformance;
    private SystemPerformanceSection systemPerformance;
    private List<String> recommendations;

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getReportVersion() {
        return reportVersion;
    }

    public void setReportVersion(String reportVersion) {
        this.reportVersion = reportVersion;
    }

    public String getExecutiveSummary() {
        return executiveSummary;
    }

    public void setExecutiveSummary(String executiveSummary) {
        this.executiveSummary = executiveSummary;
    }

    public AlgorithmPerformanceSection getAlgorithmPerformance() {
        return algorithmPerformance;
    }

    public void setAlgorithmPerformance(AlgorithmPerformanceSection algorithmPerformance) {
        this.algorithmPerformance = algorithmPerformance;
    }

    public ApiPerformanceSection getApiPerformance() {
        return apiPerformance;
    }

    public void setApiPerformance(ApiPerformanceSection apiPerformance) {
        this.apiPerformance = apiPerformance;
    }

    public SystemPerformanceSection getSystemPerformance() {
        return systemPerformance;
    }

    public void setSystemPerformance(SystemPerformanceSection systemPerformance) {
        this.systemPerformance = systemPerformance;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }
}
