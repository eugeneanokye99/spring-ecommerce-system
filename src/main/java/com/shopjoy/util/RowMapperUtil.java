package com.shopjoy.util;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Utility class providing reusable RowMapper implementations and helper methods
 * for common database mapping operations.
 * 
 * This utility reduces boilerplate code when working with JdbcTemplate
 * by providing common mapping patterns and type conversions.
 * 
 * USAGE EXAMPLES:
 * 
 * 1. Simple type mappers:
 *    List<String> names = jdbcTemplate.query(sql, RowMapperUtil.stringRowMapper("column_name"));
 *    List<Integer> ids = jdbcTemplate.query(sql, RowMapperUtil.integerRowMapper("id"));
 * 
 * 2. Timestamp conversion:
 *    LocalDateTime created = RowMapperUtil.toLocalDateTime(rs.getTimestamp("created_at"));
 * 
 * 3. Safe string retrieval:
 *    String name = RowMapperUtil.getString(rs, "name", "Unknown");
 * 
 * @author ShopJoy Team
 * @since 2.0 (Spring Migration)
 */
public final class RowMapperUtil {

    // Private constructor to prevent instantiation
    private RowMapperUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== SIMPLE TYPE ROW MAPPERS ====================

    /**
     * Creates a RowMapper for extracting a single String column.
     * 
     * Usage:
     * String sql = "SELECT username FROM users";
     * List<String> usernames = jdbcTemplate.query(sql, stringRowMapper("username"));
     * 
     * @param columnName the name of the column to extract
     * @return RowMapper that extracts the specified column as String
     */
    public static RowMapper<String> stringRowMapper(String columnName) {
        return (rs, rowNum) -> rs.getString(columnName);
    }

    /**
     * Creates a RowMapper for extracting a single Integer column.
     * 
     * @param columnName the name of the column to extract
     * @return RowMapper that extracts the specified column as Integer
     */
    public static RowMapper<Integer> integerRowMapper(String columnName) {
        return (rs, rowNum) -> {
            int value = rs.getInt(columnName);
            return rs.wasNull() ? null : value;
        };
    }

    /**
     * Creates a RowMapper for extracting a single Long column.
     * 
     * @param columnName the name of the column to extract
     * @return RowMapper that extracts the specified column as Long
     */
    public static RowMapper<Long> longRowMapper(String columnName) {
        return (rs, rowNum) -> {
            long value = rs.getLong(columnName);
            return rs.wasNull() ? null : value;
        };
    }

    /**
     * Creates a RowMapper for extracting a single Double column.
     * 
     * @param columnName the name of the column to extract
     * @return RowMapper that extracts the specified column as Double
     */
    public static RowMapper<Double> doubleRowMapper(String columnName) {
        return (rs, rowNum) -> {
            double value = rs.getDouble(columnName);
            return rs.wasNull() ? null : value;
        };
    }

    /**
     * Creates a RowMapper for extracting a single Boolean column.
     * 
     * @param columnName the name of the column to extract
     * @return RowMapper that extracts the specified column as Boolean
     */
    public static RowMapper<Boolean> booleanRowMapper(String columnName) {
        return (rs, rowNum) -> {
            boolean value = rs.getBoolean(columnName);
            return rs.wasNull() ? null : value;
        };
    }

    /**
     * Creates a RowMapper for extracting a LocalDateTime from a Timestamp column.
     * 
     * @param columnName the name of the column to extract
     * @return RowMapper that extracts the specified column as LocalDateTime
     */
    public static RowMapper<LocalDateTime> localDateTimeRowMapper(String columnName) {
        return (rs, rowNum) -> toLocalDateTime(rs.getTimestamp(columnName));
    }

    // ==================== TYPE CONVERSION HELPERS ====================

    /**
     * Safely converts a SQL Timestamp to LocalDateTime.
     * Returns null if the timestamp is null.
     * 
     * @param timestamp the SQL Timestamp to convert
     * @return LocalDateTime or null if input is null
     */
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    /**
     * Safely converts a LocalDateTime to SQL Timestamp.
     * Returns null if the LocalDateTime is null.
     * 
     * @param localDateTime the LocalDateTime to convert
     * @return Timestamp or null if input is null
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return localDateTime != null ? Timestamp.valueOf(localDateTime) : null;
    }

    // ==================== SAFE EXTRACTION METHODS ====================

    /**
     * Safely extracts a String from ResultSet with a default value.
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @param defaultValue the value to return if column is null
     * @return the column value or default value if null
     * @throws SQLException if a database access error occurs
     */
    public static String getString(ResultSet rs, String columnName, String defaultValue) throws SQLException {
        String value = rs.getString(columnName);
        return rs.wasNull() ? defaultValue : value;
    }

    /**
     * Safely extracts an Integer from ResultSet with null handling.
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @return Integer value or null if column is null
     * @throws SQLException if a database access error occurs
     */
    public static Integer getInteger(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }

    /**
     * Safely extracts a Long from ResultSet with null handling.
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @return Long value or null if column is null
     * @throws SQLException if a database access error occurs
     */
    public static Long getLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    /**
     * Safely extracts a Double from ResultSet with null handling.
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @return Double value or null if column is null
     * @throws SQLException if a database access error occurs
     */
    public static Double getDouble(ResultSet rs, String columnName) throws SQLException {
        double value = rs.getDouble(columnName);
        return rs.wasNull() ? null : value;
    }

    /**
     * Safely extracts a Boolean from ResultSet with null handling.
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @return Boolean value or null if column is null
     * @throws SQLException if a database access error occurs
     */
    public static Boolean getBoolean(ResultSet rs, String columnName) throws SQLException {
        boolean value = rs.getBoolean(columnName);
        return rs.wasNull() ? null : value;
    }

    /**
     * Safely extracts LocalDateTime from a Timestamp column with null handling.
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @return LocalDateTime value or null if column is null
     * @throws SQLException if a database access error occurs
     */
    public static LocalDateTime getLocalDateTime(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return toLocalDateTime(timestamp);
    }

    // ==================== ENUM CONVERSION HELPERS ====================

    /**
     * Safely extracts an enum value from a String column.
     * 
     * Usage:
     * UserType type = getEnum(rs, "user_type", UserType.class, UserType::fromString, UserType.CUSTOMER);
     * 
     * @param rs the ResultSet
     * @param columnName the column name
     * @param enumClass the enum class
     * @param converter function to convert String to enum (e.g., UserType::fromString)
     * @param defaultValue default enum value if column is null
     * @param <E> the enum type
     * @return enum value or default if column is null
     * @throws SQLException if a database access error occurs
     */
    public static <E extends Enum<E>> E getEnum(
            ResultSet rs, 
            String columnName, 
            Class<E> enumClass,
            java.util.function.Function<String, E> converter,
            E defaultValue) throws SQLException {
        
        String value = rs.getString(columnName);
        if (rs.wasNull() || value == null) {
            return defaultValue;
        }
        
        try {
            return converter.apply(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Converts enum to lowercase string for database storage.
     * Returns null if enum is null.
     * 
     * @param enumValue the enum value
     * @return lowercase string representation or null
     */
    public static String enumToString(Enum<?> enumValue) {
        return enumValue != null ? enumValue.toString().toLowerCase() : null;
    }

    // ==================== VALIDATION HELPERS ====================

    /**
     * Checks if a column exists in the ResultSet metadata.
     * Useful for optional columns or different database schemas.
     * 
     * @param rs the ResultSet
     * @param columnName the column name to check
     * @return true if column exists, false otherwise
     */
    public static boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
