package com.shopjoy.repository;

import com.shopjoy.entity.CartItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class CartItemRepository implements GenericRepository<CartItem, Integer> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> {
        CartItem item = new CartItem();
        item.setCartItemId(rs.getInt("cart_item_id"));
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) item.setCreatedAt(created.toLocalDateTime());
        return item;
    };

    public CartItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<CartItem> findById(Integer cartItemId) {
        if (cartItemId == null) return Optional.empty();
        String sql = "SELECT * FROM cart_items WHERE cart_item_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, cartItemId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CartItem> findAll() {
        return jdbcTemplate.query("SELECT * FROM cart_items", cartItemRowMapper);
    }

    @Override
    @Transactional(readOnly = false)
    public CartItem save(CartItem item) {
        Optional<CartItem> existing = findByUserAndProduct(item.getUserId(), item.getProductId());
        if (existing.isPresent()) {
            CartItem existingItem = existing.get();
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            return update(existingItem);
        }
        
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?) RETURNING cart_item_id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, item.getUserId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) item.setCartItemId(keyHolder.getKey().intValue());
        return item;
    }

    @Override
    @Transactional(readOnly = false)
    public CartItem update(CartItem item) {
        jdbcTemplate.update("UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?", 
            item.getQuantity(), item.getCartItemId());
        return item;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(Integer cartItemId) {
        return jdbcTemplate.update("DELETE FROM cart_items WHERE cart_item_id = ?", cartItemId) > 0;
    }

    @Override
    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_items", Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public boolean existsById(Integer cartItemId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_items WHERE cart_item_id = ?", Long.class, cartItemId);
        return count != null && count > 0;
    }

    public List<CartItem> findByUserId(int userId) {
        return jdbcTemplate.query("SELECT * FROM cart_items WHERE user_id = ? ORDER BY cart_item_id", 
            cartItemRowMapper, userId);
    }

    public Optional<CartItem> findByUserAndProduct(int userId, int productId) {
        String sql = "SELECT * FROM cart_items WHERE user_id = ? AND product_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, userId, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = false)
    public void clearCart(int userId) {
        jdbcTemplate.update("DELETE FROM cart_items WHERE user_id = ?", userId);
    }

    public long countByUserId(int userId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_items WHERE user_id = ?", Long.class, userId);
        return count != null ? count : 0L;
    }
}
