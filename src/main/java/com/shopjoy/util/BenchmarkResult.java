package com.shopjoy.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkResult {
    private String algorithmName;
    private double executionTimeMs;
    private long memoryUsedBytes;
    private int datasetSize;
    
    public String getExecutionTimeFormatted() {
        if (executionTimeMs < 1) {
            return String.format("%.3f ms", executionTimeMs);
        } else if (executionTimeMs < 1000) {
            return String.format("%.2f ms", executionTimeMs);
        } else {
            return String.format("%.2f s", executionTimeMs / 1000);
        }
    }
    
    public String getMemoryUsedFormatted() {
        if (memoryUsedBytes < 1024) {
            return memoryUsedBytes + " B";
        } else if (memoryUsedBytes < 1024 * 1024) {
            return String.format("%.2f KB", memoryUsedBytes / 1024.0);
        } else {
            return String.format("%.2f MB", memoryUsedBytes / (1024.0 * 1024.0));
        }
    }

    /**
     * Returns a formatted string summarizing the benchmark result for display.
     */
    public String getFormattedOutput() {
        return String.format("%s: %s, %s", algorithmName, getExecutionTimeFormatted(), getMemoryUsedFormatted());
    }
}
