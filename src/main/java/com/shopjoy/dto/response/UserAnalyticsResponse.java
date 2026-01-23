package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnalyticsResponse {
    private long totalOrders;
    private double totalSpent;
    private long totalItemsPurchased;
    private List<CategorySpending> spendingByCategory;
    private List<RecentActivity> recentActivities;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySpending {
        private String categoryName;
        private double amountSpent;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentActivity {
        private String description;
        private String date;
        private String type; // e.g., "PURCHASE", "REVIEW"
    }
}
