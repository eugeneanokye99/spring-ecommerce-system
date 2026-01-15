package com.shopjoy.repository;

import com.shopjoy.entity.Category;
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
public class CategoryRepository implements GenericRepository<Category, Integer> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        int parentId = rs.getInt("parent_category_id");
        category.setParentCategoryId(rs.wasNull() ? null : parentId);
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) category.setCreatedAt(created.toLocalDateTime());
        return category;
    };

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Category> findById(Integer categoryId) {
        if (categoryId == null) return Optional.empty();
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, categoryRowMapper, categoryId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findAll() {
        return jdbcTemplate.query("SELECT * FROM categories ORDER BY category_name", categoryRowMapper);
    }

    @Override
    @Transactional(readOnly = false)
    public Category save(Category category) {
        String sql = "INSERT INTO categories (category_name, description, parent_category_id) VALUES (?, ?, ?) RETURNING category_id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            if (category.getParentCategoryId() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, category.getParentCategoryId());
            }
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) category.setCategoryId(keyHolder.getKey().intValue());
        return category;
    }

    @Override
    @Transactional(readOnly = false)
    public Category update(Category category) {
        String sql = "UPDATE categories SET category_name = ?, description = ?, parent_category_id = ? WHERE category_id = ?";
        jdbcTemplate.update(sql, category.getCategoryName(), category.getDescription(), 
            category.getParentCategoryId(), category.getCategoryId());
        return category;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(Integer categoryId) {
        return jdbcTemplate.update("DELETE FROM categories WHERE category_id = ?", categoryId) > 0;
    }

    @Override
    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories", Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public boolean existsById(Integer categoryId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories WHERE category_id = ?", Long.class, categoryId);
        return count != null && count > 0;
    }

    public List<Category> findTopLevelCategories() {
        return jdbcTemplate.query("SELECT * FROM categories WHERE parent_category_id IS NULL ORDER BY category_name", categoryRowMapper);
    }

    public List<Category> findSubcategories(Integer parentCategoryId) {
        if (parentCategoryId == null) return List.of();
        return jdbcTemplate.query("SELECT * FROM categories WHERE parent_category_id = ? ORDER BY category_name", 
            categoryRowMapper, parentCategoryId);
    }

    public boolean hasSubcategories(int categoryId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories WHERE parent_category_id = ?", Long.class, categoryId);
        return count != null && count > 0;
    }

    public long countSubcategories(int categoryId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories WHERE parent_category_id = ?", Long.class, categoryId);
        return count != null ? count : 0L;
    }
}
