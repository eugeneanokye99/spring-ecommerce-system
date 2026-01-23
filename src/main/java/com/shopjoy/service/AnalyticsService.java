package com.shopjoy.service;

import com.shopjoy.dto.response.DashboardDataResponse;
import com.shopjoy.dto.response.UserAnalyticsResponse;

public interface AnalyticsService {
    DashboardDataResponse getDashboardData();

    UserAnalyticsResponse getUserAnalytics(Integer userId);
}
