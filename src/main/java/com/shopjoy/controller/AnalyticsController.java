package com.shopjoy.controller;

import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.DashboardDataResponse;
import com.shopjoy.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Analytics", description = "APIs for admin dashboard analytics and performance metrics")
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Operation(summary = "Get dashboard data", description = "Retrieves stats, sales history, and performance metrics for the admin dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardDataResponse>> getDashboardData() {
        DashboardDataResponse data = analyticsService.getDashboardData();
        return ResponseEntity.ok(ApiResponse.success(data, "Dashboard data retrieved successfully"));
    }

    @Operation(summary = "Get user analytics", description = "Retrieves personalized analytics for a specific customer")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<com.shopjoy.dto.response.UserAnalyticsResponse>> getUserAnalytics(
            @org.springframework.web.bind.annotation.PathVariable Integer userId) {
        com.shopjoy.dto.response.UserAnalyticsResponse data = analyticsService.getUserAnalytics(userId);
        return ResponseEntity.ok(ApiResponse.success(data, "User analytics retrieved successfully"));
    }
}
