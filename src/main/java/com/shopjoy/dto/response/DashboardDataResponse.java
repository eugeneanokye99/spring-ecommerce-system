package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataResponse {
    private OverallStats overallStats;
    private List<SalesDataPoint> salesOverTime;
    private List<CategorySalesDataPoint> categoryDistribution;
    private Map<String, Map<String, Object>> performanceMetrics;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverallStats {
        private long totalProducts;
        private long totalUsers;
        private long totalOrders;
        private double totalRevenue;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesDataPoint {
        private String date;
        private double revenue;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySalesDataPoint {
        private String categoryName;
        private double revenue;
        private long orderCount;
    }
}
