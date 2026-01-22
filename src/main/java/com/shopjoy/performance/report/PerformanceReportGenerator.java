package com.shopjoy.performance.report;

import com.shopjoy.performance.AlgorithmPerformanceAnalyzer;
import com.shopjoy.util.algorithm.BenchmarkResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformanceReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PerformanceReport generateReport(
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis,
        Map<String, Double> restMetrics,
        Map<String, Double> graphqlMetrics,
        Map<String, Object> systemMetrics
    ) {
        PerformanceReport report = new PerformanceReport();
        report.setGeneratedAt(LocalDateTime.now());
        report.setReportVersion("1.0.0");
        
        report.setAlgorithmPerformance(createAlgorithmSection(algorithmAnalysis));
        report.setApiPerformance(createApiSection(restMetrics, graphqlMetrics));
        report.setSystemPerformance(createSystemSection(systemMetrics));
        report.setRecommendations(generateRecommendations(algorithmAnalysis, restMetrics, graphqlMetrics));
        report.setExecutiveSummary(generateExecutiveSummary(report));
        
        return report;
    }

    public String generateMarkdownReport(PerformanceReport report) {
        StringBuilder md = new StringBuilder();
        
        md.append("# Performance Analysis Report\n\n");
        md.append("**Generated:** ").append(report.getGeneratedAt().format(DATE_FORMATTER)).append("\n");
        md.append("**Version:** ").append(report.getReportVersion()).append("\n\n");
        
        md.append("## Executive Summary\n\n");
        md.append(report.getExecutiveSummary()).append("\n\n");
        
        md.append("## Algorithm Performance\n\n");
        AlgorithmPerformanceSection algSection = report.getAlgorithmPerformance();
        md.append("**Dataset Size:** ").append(algSection.getDatasetSize()).append(" items\n\n");
        
        md.append("### Sorting Algorithms\n\n");
        md.append("| Algorithm | Time (ms) | Memory (KB) |\n");
        md.append("|-----------|-----------|-------------|\n");
        algSection.getSortingResults().forEach((name, result) -> {
            md.append("| ").append(name).append(" | ")
              .append(String.format("%.3f", result.getExecutionTimeMs())).append(" | ")
              .append(result.getMemoryUsedBytes() / 1024).append(" |\n");
        });
        
        md.append("\n### Search Algorithms\n\n");
        md.append("| Algorithm | Time (ms) | Memory (KB) |\n");
        md.append("|-----------|-----------|-------------|\n");
        algSection.getSearchResults().forEach((name, result) -> {
            md.append("| ").append(name).append(" | ")
              .append(String.format("%.3f", result.getExecutionTimeMs())).append(" | ")
              .append(result.getMemoryUsedBytes() / 1024).append(" |\n");
        });
        
        md.append("\n**Best Sorting:** ").append(algSection.getBestSortingAlgorithm()).append("\n");
        md.append("**Best Search:** ").append(algSection.getBestSearchAlgorithm()).append("\n\n");
        
        md.append("## API Performance Comparison\n\n");
        ApiPerformanceSection apiSection = report.getApiPerformance();
        
        md.append("### REST API\n\n");
        apiSection.getRestMetrics().forEach((metric, value) -> {
            md.append("- **").append(metric).append(":** ").append(String.format("%.3f", value)).append("\n");
        });
        
        md.append("\n### GraphQL API\n\n");
        apiSection.getGraphqlMetrics().forEach((metric, value) -> {
            md.append("- **").append(metric).append(":** ").append(String.format("%.3f", value)).append("\n");
        });
        
        md.append("\n**Winner:** ").append(apiSection.getWinner()).append("\n");
        md.append("**Performance Gain:** ").append(String.format("%.2f%%", apiSection.getPerformanceGain())).append("\n\n");
        
        md.append("## Recommendations\n\n");
        report.getRecommendations().forEach(rec -> md.append("- ").append(rec).append("\n"));
        
        return md.toString();
    }

    public String generateHtmlReport(PerformanceReport report) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<title>Performance Analysis Report</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; }\n");
        html.append("h1 { color: #333; }\n");
        html.append("table { border-collapse: collapse; width: 100%; margin: 20px 0; }\n");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
        html.append("th { background-color: #4CAF50; color: white; }\n");
        html.append(".summary { background-color: #f9f9f9; padding: 20px; margin: 20px 0; }\n");
        html.append(".recommendation { background-color: #e7f3fe; padding: 10px; margin: 10px 0; }\n");
        html.append("</style>\n</head>\n<body>\n");
        
        html.append("<h1>Performance Analysis Report</h1>\n");
        html.append("<p><strong>Generated:</strong> ").append(report.getGeneratedAt().format(DATE_FORMATTER)).append("</p>\n");
        
        html.append("<div class='summary'>\n");
        html.append("<h2>Executive Summary</h2>\n");
        html.append("<p>").append(report.getExecutiveSummary()).append("</p>\n");
        html.append("</div>\n");
        
        html.append("<h2>Algorithm Performance</h2>\n");
        AlgorithmPerformanceSection algSection = report.getAlgorithmPerformance();
        
        html.append("<h3>Sorting Algorithms (").append(algSection.getDatasetSize()).append(" items)</h3>\n");
        html.append("<table>\n<tr><th>Algorithm</th><th>Time (ms)</th><th>Memory (KB)</th></tr>\n");
        algSection.getSortingResults().forEach((name, result) -> {
            html.append("<tr><td>").append(name).append("</td>")
                .append("<td>").append(String.format("%.3f", result.getExecutionTimeMs())).append("</td>")
                .append("<td>").append(result.getMemoryUsedBytes() / 1024).append("</td></tr>\n");
        });
        html.append("</table>\n");
        
        html.append("<h2>API Performance Comparison</h2>\n");
        ApiPerformanceSection apiSection = report.getApiPerformance();
        html.append("<p><strong>Winner:</strong> ").append(apiSection.getWinner()).append("</p>\n");
        html.append("<p><strong>Performance Gain:</strong> ").append(String.format("%.2f%%", apiSection.getPerformanceGain())).append("</p>\n");
        
        html.append("<h2>Recommendations</h2>\n");
        report.getRecommendations().forEach(rec -> {
            html.append("<div class='recommendation'>").append(rec).append("</div>\n");
        });
        
        html.append("</body>\n</html>");
        
        return html.toString();
    }

    public String generateCsvReport(PerformanceReport report) {
        StringBuilder csv = new StringBuilder();
        
        csv.append("Report Type,Metric,Value\n");
        csv.append("Metadata,Generated At,").append(report.getGeneratedAt().format(DATE_FORMATTER)).append("\n");
        csv.append("Metadata,Version,").append(report.getReportVersion()).append("\n\n");
        
        AlgorithmPerformanceSection algSection = report.getAlgorithmPerformance();
        csv.append("Algorithm Performance,Dataset Size,").append(algSection.getDatasetSize()).append("\n");
        
        algSection.getSortingResults().forEach((name, result) -> {
            csv.append("Sorting,").append(name).append(" Time (ms),").append(result.getExecutionTimeMs()).append("\n");
            csv.append("Sorting,").append(name).append(" Memory (KB),").append(result.getMemoryUsedBytes() / 1024).append("\n");
        });
        
        ApiPerformanceSection apiSection = report.getApiPerformance();
        apiSection.getRestMetrics().forEach((metric, value) -> {
            csv.append("REST API,").append(metric).append(",").append(value).append("\n");
        });
        
        apiSection.getGraphqlMetrics().forEach((metric, value) -> {
            csv.append("GraphQL API,").append(metric).append(",").append(value).append("\n");
        });
        
        return csv.toString();
    }

    private AlgorithmPerformanceSection createAlgorithmSection(AlgorithmPerformanceAnalyzer.AnalysisResult analysis) {
        AlgorithmPerformanceSection section = new AlgorithmPerformanceSection();
        section.setDatasetSize(analysis.getDatasetSize());
        
        Map<String, BenchmarkResult> sortingResults = new HashMap<>();
        sortingResults.put("QuickSort", analysis.getQuickSortResult());
        sortingResults.put("MergeSort", analysis.getMergeSortResult());
        sortingResults.put("HeapSort", analysis.getHeapSortResult());
        section.setSortingResults(sortingResults);
        
        Map<String, BenchmarkResult> searchResults = new HashMap<>();
        searchResults.put("BinarySearch", analysis.getBinarySearchResult());
        searchResults.put("LinearSearch", analysis.getLinearSearchResult());
        searchResults.put("JumpSearch", analysis.getJumpSearchResult());
        section.setSearchResults(searchResults);
        
        section.setBestSortingAlgorithm(analysis.getBestSortingAlgorithm());
        section.setBestSearchAlgorithm(analysis.getBestSearchAlgorithm());
        
        return section;
    }

    private ApiPerformanceSection createApiSection(Map<String, Double> restMetrics, Map<String, Double> graphqlMetrics) {
        ApiPerformanceSection section = new ApiPerformanceSection();
        section.setRestMetrics(restMetrics);
        section.setGraphqlMetrics(graphqlMetrics);
        
        double restAvg = restMetrics.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double graphqlAvg = graphqlMetrics.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
        
        if (restAvg < graphqlAvg) {
            section.setWinner("REST");
            section.setPerformanceGain(((graphqlAvg - restAvg) / graphqlAvg) * 100);
        } else {
            section.setWinner("GraphQL");
            section.setPerformanceGain(((restAvg - graphqlAvg) / restAvg) * 100);
        }
        
        return section;
    }

    private SystemPerformanceSection createSystemSection(Map<String, Object> systemMetrics) {
        SystemPerformanceSection section = new SystemPerformanceSection();
        section.setMetrics(systemMetrics);
        return section;
    }

    private List<String> generateRecommendations(
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis,
        Map<String, Double> restMetrics,
        Map<String, Double> graphqlMetrics
    ) {
        List<String> recommendations = new ArrayList<>(algorithmAnalysis.getRecommendations());
        
        double restAvg = restMetrics.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double graphqlAvg = graphqlMetrics.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
        
        if (graphqlAvg < restAvg * 0.8) {
            recommendations.add("Consider migrating complex queries to GraphQL for better performance");
        }
        
        if (algorithmAnalysis.getDatasetSize() > 5000) {
            recommendations.add("Implement caching for large dataset operations");
            recommendations.add("Consider database-level sorting for datasets over 5000 items");
        }
        
        return recommendations;
    }

    private String generateExecutiveSummary(PerformanceReport report) {
        StringBuilder summary = new StringBuilder();
        
        AlgorithmPerformanceSection algSection = report.getAlgorithmPerformance();
        ApiPerformanceSection apiSection = report.getApiPerformance();
        
        summary.append("Performance analysis completed for ").append(algSection.getDatasetSize())
               .append(" data items. ");
        
        summary.append(algSection.getBestSortingAlgorithm()).append(" demonstrated optimal sorting performance, ");
        summary.append("while ").append(algSection.getBestSearchAlgorithm()).append(" excelled in search operations. ");
        
        summary.append("API comparison shows ").append(apiSection.getWinner())
               .append(" with ").append(String.format("%.2f%%", apiSection.getPerformanceGain()))
               .append(" performance advantage. ");
        
        summary.append("Total ").append(report.getRecommendations().size())
               .append(" optimization recommendations generated.");
        
        return summary.toString();
    }
}
