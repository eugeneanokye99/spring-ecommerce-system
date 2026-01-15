package com.shopjoy.repository;

import com.shopjoy.entity.User;
import com.shopjoy.entity.UserType;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class UserRepository implements GenericRepository<User, Integer> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhone(rs.getString("phone"));
        user.setUserType(UserType.fromString(rs.getString("user_type")));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        if (rs.getTimestamp("updated_at") != null) {
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return user;
    };

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(Integer userId) {
        if (userId == null) return Optional.empty();
        
        String sql = """
                SELECT user_id, username, email, password_hash, first_name, last_name, 
                       phone, user_type, created_at, updated_at
                FROM users WHERE user_id = ?
                """;
        
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = """
                SELECT user_id, username, email, password_hash, first_name, last_name, 
                       phone, user_type, created_at, updated_at
                FROM users ORDER BY username
                """;
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    @Transactional(readOnly = false)
    public User save(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt());
        
        String sql = """
                INSERT INTO users (username, email, password_hash, first_name, last_name, 
                                 phone, user_type, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING user_id
                """;
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashedPassword);
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getUserType().toString().toLowerCase());
            ps.setObject(8, user.getCreatedAt());
            ps.setObject(9, user.getUpdatedAt());
            return ps;
        }, keyHolder);
        
        user.setUserId(keyHolder.getKey().intValue());
        user.setPasswordHash(hashedPassword);
        return user;
    }

    @Override
    @Transactional(readOnly = false)
    public User update(User user) {
        String sql = """
                UPDATE users 
                SET email = ?, first_name = ?, last_name = ?, phone = ?, 
                    user_type = ?, updated_at = CURRENT_TIMESTAMP
                WHERE user_id = ?
                """;
        
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getUserType().toString().toLowerCase(),
                user.getUserId());
        
        return user;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(Integer userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId) > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM users";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public boolean existsById(Integer userId) {
        if (userId == null) return false;
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, userId);
        return count != null && count > 0;
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        
        String sql = """
                SELECT user_id, username, email, password_hash, first_name, last_name, 
                       phone, user_type, created_at, updated_at
                FROM users WHERE username = ?
                """;
        
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, username));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        
        String sql = """
                SELECT user_id, username, email, password_hash, first_name, last_name, 
                       phone, user_type, created_at, updated_at
                FROM users WHERE email = ?
                """;
        
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isEmpty()) return Optional.empty();
        
        User user = userOpt.get();
        boolean passwordMatches = BCrypt.checkpw(password, user.getPasswordHash());
        return passwordMatches ? Optional.of(user) : Optional.empty();
    }

    public boolean emailExists(String email) {
        if (email == null) return false;
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, email);
        return count != null && count > 0;
    }

    public boolean usernameExists(String username) {
        if (username == null) return false;
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, username);
        return count != null && count > 0;
    }

    public List<User> findByUserType(UserType userType) {
        if (userType == null) return List.of();
        
        String sql = """
                SELECT user_id, username, email, password_hash, first_name, last_name, 
                       phone, user_type, created_at, updated_at
                FROM users WHERE user_type = ? ORDER BY username
                """;
        
        return jdbcTemplate.query(sql, userRowMapper, userType.toString().toLowerCase());
    }

    @Transactional(readOnly = false)
    public boolean changePassword(int userId, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        return jdbcTemplate.update(sql, hashedPassword, userId) > 0;
    }
}
