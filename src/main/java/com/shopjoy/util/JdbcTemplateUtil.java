package com.shopjoy.util;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Utility class providing common JdbcTemplate patterns and helper methods.
 * 
 * This utility encapsulates frequently-used JdbcTemplate operations
 * to reduce boilerplate code in repository classes.
 * 
 * COMMON PATTERNS PROVIDED:
 * 1. Optional-based queries (handles EmptyResultDataAccessException)
 * 2. Existence checks (efficient COUNT queries)
 * 3. Key generation for INSERT operations
 * 4. Null-safe parameter handling
 * 
 * USAGE EXAMPLES:
 * 
 * // Find by ID returning Optional
 * Optional<User> user = JdbcTemplateUtil.queryForOptional(
 *     jdbcTemplate, sql, userRowMapper, userId
 * );
 * 
 * // Check existence
 * boolean exists = JdbcTemplateUtil.exists(
 *     jdbcTemplate, "SELECT 1 FROM users WHERE email = ?", email
 * );
 * 
 * // Execute INSERT and get generated ID
 * Integer generatedId = JdbcTemplateUtil.insertAndReturnId(
 *     jdbcTemplate, sql, ps -> {
 *         ps.setString(1, username);
 *         ps.setString(2, email);
 *     }
 * );
 * 
 * @author ShopJoy Team
 * @since 2.0 (Spring Migration)
 */
public final class JdbcTemplateUtil {

    // Private constructor to prevent instantiation
    private JdbcTemplateUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== OPTIONAL-BASED QUERY METHODS ====================

    /**
     * Executes a query expected to return a single object wrapped in Optional.
     * 
     * This is the recommended approach for queries that may return no results,
     * as it handles the EmptyResultDataAccessException gracefully.
     * 
     * Usage:
     * Optional<User> user = queryForOptional(
     *     jdbcTemplate, 
     *     "SELECT * FROM users WHERE user_id = ?",
     *     userRowMapper,
     *     userId
     * );
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the SQL query
     * @param rowMapper the RowMapper to convert ResultSet to object
     * @param args query parameters
     * @param <T> the type of object to return
     * @return Optional containing the result, or empty if no result found
     */
    public static <T> Optional<T> queryForOptional(
            JdbcTemplate jdbcTemplate,
            String sql,
            RowMapper<T> rowMapper,
            Object... args) {
        
        try {
            T result = jdbcTemplate.queryForObject(sql, rowMapper, args);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Executes a query that returns a list, ensuring non-null result.
     * 
     * JdbcTemplate.query() already returns empty list for no results,
     * but this method makes the contract explicit.
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the SQL query
     * @param rowMapper the RowMapper to convert ResultSet rows
     * @param args query parameters
     * @param <T> the type of objects in the list
     * @return List of results (empty if no results, never null)
     */
    public static <T> List<T> queryForList(
            JdbcTemplate jdbcTemplate,
            String sql,
            RowMapper<T> rowMapper,
            Object... args) {
        
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    // ==================== EXISTENCE CHECK METHODS ====================

    /**
     * Checks if at least one row exists matching the query.
     * 
     * More efficient than fetching full object when you only need existence check.
     * Uses COUNT(*) or SELECT 1 patterns.
     * 
     * Usage:
     * boolean emailExists = exists(
     *     jdbcTemplate,
     *     "SELECT 1 FROM users WHERE email = ?",
     *     email
     * );
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the SQL query (should be SELECT 1 or COUNT(*))
     * @param args query parameters
     * @return true if at least one row exists, false otherwise
     */
    public static boolean exists(JdbcTemplate jdbcTemplate, String sql, Object... args) {
        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, args);
            return result != null && result > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    /**
     * Checks if a record exists by ID using a simple COUNT query.
     * 
     * Convenience method for the common pattern of checking existence by ID.
     * 
     * Usage:
     * boolean exists = existsById(jdbcTemplate, "users", "user_id", 123);
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param tableName the table name
     * @param idColumnName the ID column name
     * @param id the ID value to check
     * @return true if record exists, false otherwise
     */
    public static boolean existsById(
            JdbcTemplate jdbcTemplate,
            String tableName,
            String idColumnName,
            Object id) {
        
        if (id == null) {
            return false;
        }
        
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", tableName, idColumnName);
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != null && count > 0;
    }

    // ==================== COUNT METHODS ====================

    /**
     * Executes a COUNT query and returns the result.
     * 
     * Safe handling of null results (returns 0).
     * 
     * Usage:
     * long userCount = count(jdbcTemplate, "SELECT COUNT(*) FROM users WHERE active = ?", true);
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the COUNT SQL query
     * @param args query parameters
     * @return the count result (0 if no results)
     */
    public static long count(JdbcTemplate jdbcTemplate, String sql, Object... args) {
        Long count = jdbcTemplate.queryForObject(sql, Long.class, args);
        return count != null ? count : 0L;
    }

    /**
     * Counts all records in a table.
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param tableName the table name
     * @return total count of records
     */
    public static long countAll(JdbcTemplate jdbcTemplate, String tableName) {
        String sql = String.format("SELECT COUNT(*) FROM %s", tableName);
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    // ==================== INSERT WITH KEY GENERATION ====================

    /**
     * Executes an INSERT statement and returns the generated key.
     * 
     * Use this for INSERT operations that need to retrieve the auto-generated ID.
     * Supports both RETURNING clause (PostgreSQL) and JDBC getGeneratedKeys().
     * 
     * Usage:
     * Integer userId = insertAndReturnId(jdbcTemplate, sql, ps -> {
     *     ps.setString(1, username);
     *     ps.setString(2, email);
     *     ps.setString(3, passwordHash);
     * });
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the INSERT SQL with RETURNING clause or auto-increment column
     * @param psSetter PreparedStatementSetter to bind parameters
     * @return the generated key as Integer, or null if not available
     */
    public static Integer insertAndReturnId(
            JdbcTemplate jdbcTemplate,
            String sql,
            PreparedStatementSetter psSetter) {
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psSetter.setValues(ps);
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    /**
     * Executes an INSERT statement and returns the generated key as Long.
     * 
     * Use this variant for tables with BIGINT/BIGSERIAL primary keys.
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the INSERT SQL
     * @param psSetter PreparedStatementSetter to bind parameters
     * @return the generated key as Long, or null if not available
     */
    public static Long insertAndReturnLongId(
            JdbcTemplate jdbcTemplate,
            String sql,
            PreparedStatementSetter psSetter) {
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psSetter.setValues(ps);
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    // ==================== UPDATE/DELETE HELPERS ====================

    /**
     * Executes an UPDATE or DELETE and returns whether any rows were affected.
     * 
     * Useful for operations where you need to know if the record existed.
     * 
     * Usage:
     * boolean deleted = updateOrDelete(jdbcTemplate, "DELETE FROM users WHERE user_id = ?", userId);
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the UPDATE or DELETE SQL
     * @param args query parameters
     * @return true if at least one row was affected, false otherwise
     */
    public static boolean updateOrDelete(JdbcTemplate jdbcTemplate, String sql, Object... args) {
        int rowsAffected = jdbcTemplate.update(sql, args);
        return rowsAffected > 0;
    }

    /**
     * Executes an UPDATE and returns the number of rows affected.
     * 
     * @param jdbcTemplate the JdbcTemplate instance
     * @param sql the UPDATE SQL
     * @param args query parameters
     * @return number of rows affected
     */
    public static int update(JdbcTemplate jdbcTemplate, String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    // ==================== FUNCTIONAL INTERFACES ====================

    /**
     * Functional interface for setting parameters on PreparedStatement.
     * 
     * Similar to Spring's PreparedStatementSetter but with simpler usage.
     */
    @FunctionalInterface
    public interface PreparedStatementSetter {
        /**
         * Set parameters on the PreparedStatement.
         * 
         * @param ps the PreparedStatement to set parameters on
         * @throws java.sql.SQLException if a database error occurs
         */
        void setValues(PreparedStatement ps) throws java.sql.SQLException;
    }

    // ==================== NULL-SAFE PARAMETER HELPERS ====================

    /**
     * Converts null values to SQL NULL for proper handling in queries.
     * 
     * This is typically not needed as JdbcTemplate handles nulls correctly,
     * but can be useful for explicit null handling in complex scenarios.
     * 
     * @param value the value to convert
     * @return the value or null
     */
    public static Object toSqlParameter(Object value) {
        return value;
    }

    /**
     * Creates a varargs array from nullable parameters.
     * Useful when building parameter arrays dynamically.
     * 
     * @param params the parameters
     * @return array of parameters
     */
    public static Object[] params(Object... params) {
        return params;
    }

    // ==================== TRANSACTION HELPERS ====================

    /**
     * Checks if the current thread is in an active transaction.
     * 
     * This can be useful for debugging or conditional logic based on transaction state.
     * Requires Spring's transaction management to be active.
     * 
     * @return true if in active transaction, false otherwise
     */
    public static boolean isInTransaction() {
        try {
            return org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive();
        } catch (Exception e) {
            return false;
        }
    }
}
