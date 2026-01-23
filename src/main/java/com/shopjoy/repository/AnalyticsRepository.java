package com.shopjoy.repository;

import com.shopjoy.dto.response.DashboardDataResponse;
import com.shopjoy.dto.response.UserAnalyticsResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public AnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Double getTotalRevenue() {
        return jdbcTemplate.queryForObject(
                "SELECT SUM(total_amount) FROM orders WHERE status NOT IN ('cancelled')", Double.class);
    }

    public List<DashboardDataResponse.SalesDataPoint> getSalesOverTime() {
        String sql = "SELECT TO_CHAR(order_date, 'YYYY-MM-DD') as date, SUM(total_amount) as revenue " +
                "FROM orders " +
                "WHERE status NOT IN ('cancelled') " +
                "GROUP BY TO_CHAR(order_date, 'YYYY-MM-DD') " +
                "ORDER BY date DESC LIMIT 7";

        return jdbcTemplate.query(sql, (rs, _) -> DashboardDataResponse.SalesDataPoint.builder()
                .date(rs.getString("date"))
                .revenue(rs.getDouble("revenue"))
                .build());
    }

    public List<DashboardDataResponse.CategorySalesDataPoint> getCategoryDistribution() {
        String sql = "SELECT c.category_name, SUM(oi.subtotal) as revenue, COUNT(DISTINCT o.order_id) as order_count " +
                "FROM categories c " +
                "JOIN products p ON c.category_id = p.category_id " +
                "JOIN order_items oi ON p.product_id = oi.product_id " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE o.status NOT IN ('cancelled') " +
                "GROUP BY c.category_name " +
                "ORDER BY revenue DESC";

        return jdbcTemplate.query(sql, (rs, _) -> DashboardDataResponse.CategorySalesDataPoint.builder()
                .categoryName(rs.getString("category_name"))
                .revenue(rs.getDouble("revenue"))
                .orderCount(rs.getLong("order_count"))
                .build());
    }

    public Long getUserTotalOrders(Integer userId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM orders WHERE user_id = ?", Long.class, userId);
    }

    public Double getUserTotalSpent(Integer userId) {
        return jdbcTemplate.queryForObject(
                "SELECT SUM(total_amount) FROM orders WHERE user_id = ? AND status NOT IN ('cancelled')",
                Double.class, userId);
    }

    public Long getUserTotalItems(Integer userId) {
        return jdbcTemplate.queryForObject(
                "SELECT SUM(quantity) FROM order_items oi JOIN orders o ON oi.order_id = o.order_id " +
                        "WHERE o.user_id = ? AND o.status NOT IN ('cancelled')",
                Long.class, userId);
    }

    public List<UserAnalyticsResponse.CategorySpending> getUserCategorySpending(Integer userId) {
        String sql = "SELECT c.category_name, SUM(oi.subtotal) as spent " +
                "FROM categories c " +
                "JOIN products p ON c.category_id = p.category_id " +
                "JOIN order_items oi ON p.product_id = oi.product_id " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE o.user_id = ? AND o.status NOT IN ('cancelled') " +
                "GROUP BY c.category_name";

        return jdbcTemplate.query(sql, (rs, _) -> UserAnalyticsResponse.CategorySpending.builder()
                .categoryName(rs.getString("category_name"))
                .amountSpent(rs.getDouble("spent"))
                .build(), userId);
    }

    public List<UserAnalyticsResponse.RecentActivity> getUserRecentActivity(Integer userId) {
        String sql = "(SELECT 'Purchased ' || p.product_name as description, TO_CHAR(o.order_date, 'YYYY-MM-DD') as date, 'PURCHASE' as type "
                + "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id JOIN products p ON oi.product_id = p.product_id "
                + "WHERE o.user_id = ?) " +
                "UNION " +
                "(SELECT 'Reviewed ' || p.product_name as description, TO_CHAR(r.created_at, 'YYYY-MM-DD') as date, 'REVIEW' as type "
                + "FROM reviews r JOIN products p ON r.product_id = p.product_id " +
                "WHERE r.user_id = ?) " +
                "ORDER BY date DESC LIMIT 5";

        return jdbcTemplate.query(sql, (rs, _) -> UserAnalyticsResponse.RecentActivity.builder()
                .description(rs.getString("description"))
                .date(rs.getString("date"))
                .type(rs.getString("type"))
                .build(), userId, userId);
    }
}
