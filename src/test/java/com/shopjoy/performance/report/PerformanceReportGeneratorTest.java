package com.shopjoy.performance.report;

import com.shopjoy.performance.AlgorithmPerformanceAnalyzer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PerformanceReportGeneratorTest {

    private PerformanceReportGenerator generator;
    private AlgorithmPerformanceAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        generator = new PerformanceReportGenerator();
        analyzer = new AlgorithmPerformanceAnalyzer();
    }

    @Test
    void generateCompleteReport() {
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis = 
            analyzer.analyzeAllAlgorithms(1000);

        Map<String, Double> restMetrics = new HashMap<>();
        restMetrics.put("Average Response Time", 45.5);
        restMetrics.put("P95 Response Time", 89.2);
        restMetrics.put("Throughput", 1250.0);

        Map<String, Double> graphqlMetrics = new HashMap<>();
        graphqlMetrics.put("Average Response Time", 38.2);
        graphqlMetrics.put("P95 Response Time", 76.5);
        graphqlMetrics.put("Throughput", 1450.0);

        Map<String, Object> systemMetrics = new HashMap<>();
        systemMetrics.put("CPU Usage", 45.2);
        systemMetrics.put("Memory Usage", 67.8);
        systemMetrics.put("Connection Pool Size", 20);

        PerformanceReport report = generator.generateReport(
            algorithmAnalysis,
            restMetrics,
            graphqlMetrics,
            systemMetrics
        );

        assertThat(report).isNotNull();
        assertThat(report.getGeneratedAt()).isNotNull();
        assertThat(report.getExecutiveSummary()).isNotEmpty();
        assertThat(report.getAlgorithmPerformance()).isNotNull();
        assertThat(report.getApiPerformance()).isNotNull();
        assertThat(report.getRecommendations()).isNotEmpty();
    }

    @Test
    void generateMarkdownReport() throws IOException {
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis = 
            analyzer.analyzeAllAlgorithms(500);

        Map<String, Double> restMetrics = new HashMap<>();
        restMetrics.put("Average Response Time", 50.0);
        
        Map<String, Double> graphqlMetrics = new HashMap<>();
        graphqlMetrics.put("Average Response Time", 40.0);

        Map<String, Object> systemMetrics = new HashMap<>();

        PerformanceReport report = generator.generateReport(
            algorithmAnalysis,
            restMetrics,
            graphqlMetrics,
            systemMetrics
        );

        String markdown = generator.generateMarkdownReport(report);

        assertThat(markdown).contains("# Performance Analysis Report");
        assertThat(markdown).contains("## Executive Summary");
        assertThat(markdown).contains("## Algorithm Performance");
        assertThat(markdown).contains("## API Performance Comparison");
        assertThat(markdown).contains("## Recommendations");

        Files.write(Paths.get("performance-report.md"), markdown.getBytes());
        System.out.println("Markdown report generated: performance-report.md");
    }

    @Test
    void generateHtmlReport() throws IOException {
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis = 
            analyzer.analyzeAllAlgorithms(500);

        Map<String, Double> restMetrics = new HashMap<>();
        restMetrics.put("Average Response Time", 50.0);
        
        Map<String, Double> graphqlMetrics = new HashMap<>();
        graphqlMetrics.put("Average Response Time", 40.0);

        Map<String, Object> systemMetrics = new HashMap<>();

        PerformanceReport report = generator.generateReport(
            algorithmAnalysis,
            restMetrics,
            graphqlMetrics,
            systemMetrics
        );

        String html = generator.generateHtmlReport(report);

        assertThat(html).contains("<!DOCTYPE html>");
        assertThat(html).contains("<title>Performance Analysis Report</title>");
        assertThat(html).contains("<h1>Performance Analysis Report</h1>");
        assertThat(html).contains("<table>");

        Files.write(Paths.get("performance-report.html"), html.getBytes());
        System.out.println("HTML report generated: performance-report.html");
    }

    @Test
    void generateCsvReport() throws IOException {
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis = 
            analyzer.analyzeAllAlgorithms(500);

        Map<String, Double> restMetrics = new HashMap<>();
        restMetrics.put("Average Response Time", 50.0);
        
        Map<String, Double> graphqlMetrics = new HashMap<>();
        graphqlMetrics.put("Average Response Time", 40.0);

        Map<String, Object> systemMetrics = new HashMap<>();

        PerformanceReport report = generator.generateReport(
            algorithmAnalysis,
            restMetrics,
            graphqlMetrics,
            systemMetrics
        );

        String csv = generator.generateCsvReport(report);

        assertThat(csv).contains("Report Type,Metric,Value");
        assertThat(csv).contains("Metadata,Generated At,");
        assertThat(csv).contains("Algorithm Performance,Dataset Size,");
        assertThat(csv).contains("REST API,");
        assertThat(csv).contains("GraphQL API,");

        Files.write(Paths.get("performance-report.csv"), csv.getBytes());
        System.out.println("CSV report generated: performance-report.csv");
    }

    @Test
    void verifyRecommendationsGeneration() {
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis = 
            analyzer.analyzeAllAlgorithms(6000);

        Map<String, Double> restMetrics = new HashMap<>();
        restMetrics.put("Average Response Time", 80.0);
        
        Map<String, Double> graphqlMetrics = new HashMap<>();
        graphqlMetrics.put("Average Response Time", 50.0);

        Map<String, Object> systemMetrics = new HashMap<>();

        PerformanceReport report = generator.generateReport(
            algorithmAnalysis,
            restMetrics,
            graphqlMetrics,
            systemMetrics
        );

        assertThat(report.getRecommendations()).isNotEmpty();
        assertThat(report.getRecommendations()).anyMatch(rec -> 
            rec.contains("GraphQL") || rec.contains("caching") || rec.contains("database"));
        
        System.out.println("\nGenerated Recommendations:");
        report.getRecommendations().forEach(rec -> System.out.println("- " + rec));
    }

    @Test
    void verifyApiPerformanceComparison() {
        AlgorithmPerformanceAnalyzer.AnalysisResult algorithmAnalysis = 
            analyzer.analyzeAllAlgorithms(100);

        Map<String, Double> restMetrics = new HashMap<>();
        restMetrics.put("Single Request", 50.0);
        restMetrics.put("List Request", 120.0);
        
        Map<String, Double> graphqlMetrics = new HashMap<>();
        graphqlMetrics.put("Single Request", 45.0);
        graphqlMetrics.put("Complex Query", 80.0);

        Map<String, Object> systemMetrics = new HashMap<>();

        PerformanceReport report = generator.generateReport(
            algorithmAnalysis,
            restMetrics,
            graphqlMetrics,
            systemMetrics
        );

        ApiPerformanceSection apiSection = report.getApiPerformance();
        
        assertThat(apiSection.getWinner()).isIn("REST", "GraphQL");
        assertThat(apiSection.getPerformanceGain()).isGreaterThan(0);
        
        System.out.println("\nAPI Performance Winner: " + apiSection.getWinner());
        System.out.println("Performance Gain: " + apiSection.getPerformanceGain() + "%");
    }
}
