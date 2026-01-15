package com.shopjoy.repository;

import com.shopjoy.entity.Product;
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
public class ProductRepository implements GenericRepository<Product, Integer> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setProductName(rs.getString("product_name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        if (rs.getTimestamp("updated_at") != null) {
            product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return product;
    };

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Product> findById(Integer productId) {
        if (productId == null) return Optional.empty();
        
        String sql = """
                SELECT product_id, category_id, product_name, description, price, image_url, 
                       created_at, updated_at
                FROM products WHERE product_id = ?
                """;
        
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, productRowMapper, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = """
                SELECT product_id, category_id, product_name, description, price, image_url, 
                       created_at, updated_at
                FROM products ORDER BY product_name
                """;
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    @Transactional(readOnly = false)
    public Product save(Product product) {
        String sql = """
                INSERT INTO products (category_id, product_name, description, price, image_url, 
                                    created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING product_id
                """;
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            ps.setString(5, product.getImageUrl());
            ps.setObject(6, product.getCreatedAt());
            ps.setObject(7, product.getUpdatedAt());
            return ps;
        }, keyHolder);
        
        product.setProductId(keyHolder.getKey().intValue());
        return product;
    }

    @Override
    @Transactional(readOnly = false)
    public Product update(Product product) {
        String sql = """
                UPDATE products 
                SET category_id = ?, product_name = ?, description = ?, price = ?, 
                    image_url = ?, updated_at = CURRENT_TIMESTAMP
                WHERE product_id = ?
                """;
        
        jdbcTemplate.update(sql,
                product.getCategoryId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getProductId());
        
        return product;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(Integer productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        return jdbcTemplate.update(sql, productId) > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM products";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public boolean existsById(Integer productId) {
        if (productId == null) return false;
        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, productId);
        return count != null && count > 0;
    }

    public List<Product> findByCategoryId(Integer categoryId) {
        String sql = """
                SELECT product_id, category_id, product_name, description, price, image_url, 
                       created_at, updated_at
                FROM products WHERE category_id = ? ORDER BY product_name
                """;
        return jdbcTemplate.query(sql, productRowMapper, categoryId);
    }

    public List<Product> findByNameContaining(String keyword) {
        String sql = """
                SELECT product_id, category_id, product_name, description, price, image_url, 
                       created_at, updated_at
                FROM products WHERE product_name ILIKE ? ORDER BY product_name
                """;
        return jdbcTemplate.query(sql, productRowMapper, "%" + keyword + "%");
    }

    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        String sql = """
                SELECT product_id, category_id, product_name, description, price, image_url, 
                       created_at, updated_at
                FROM products WHERE price BETWEEN ? AND ? ORDER BY price
                """;
        return jdbcTemplate.query(sql, productRowMapper, minPrice, maxPrice);
    }

    @Transactional(readOnly = false)
    public int[][] batchInsert(List<Product> products) {
        String sql = """
                INSERT INTO products (category_id, product_name, description, price, image_url, 
                                    created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        
        return jdbcTemplate.batchUpdate(sql, products, products.size(),
                (ps, product) -> {
                    ps.setInt(1, product.getCategoryId());
                    ps.setString(2, product.getProductName());
                    ps.setString(3, product.getDescription());
                    ps.setDouble(4, product.getPrice());
                    ps.setString(5, product.getImageUrl());
                    ps.setObject(6, product.getCreatedAt());
                    ps.setObject(7, product.getUpdatedAt());
                });
    }

    public long countByCategory(Integer categoryId) {
        String sql = "SELECT COUNT(*) FROM products WHERE category_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, categoryId);
        return count != null ? count : 0L;
    }
}
