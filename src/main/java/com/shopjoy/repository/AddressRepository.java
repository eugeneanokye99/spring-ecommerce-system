package com.shopjoy.repository;

import com.shopjoy.entity.Address;
import com.shopjoy.entity.AddressType;
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
public class AddressRepository implements GenericRepository<Address, Integer> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Address> addressRowMapper = (rs, rowNum) -> {
        Address address = new Address();
        address.setAddressId(rs.getInt("address_id"));
        address.setUserId(rs.getInt("user_id"));
        String typeStr = rs.getString("address_type");
        address.setAddressType(typeStr != null ? AddressType.valueOf(typeStr.toUpperCase()) : null);
        address.setStreetAddress(rs.getString("street_address"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setPostalCode(rs.getString("postal_code"));
        address.setCountry(rs.getString("country"));
        address.setDefault(rs.getBoolean("is_default"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) address.setCreatedAt(created.toLocalDateTime());
        return address;
    };

    public AddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Address> findById(Integer addressId) {
        if (addressId == null) return Optional.empty();
        String sql = "SELECT * FROM addresses WHERE address_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, addressRowMapper, addressId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Address> findAll() {
        return jdbcTemplate.query("SELECT * FROM addresses", addressRowMapper);
    }

    @Override
    @Transactional(readOnly = false)
    public Address save(Address address) {
        String sql = "INSERT INTO addresses (user_id, address_type, street_address, city, state, postal_code, country, is_default) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING address_id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getAddressType() != null ? address.getAddressType().name().toLowerCase() : null);
            ps.setString(3, address.getStreetAddress());
            ps.setString(4, address.getCity());
            ps.setString(5, address.getState());
            ps.setString(6, address.getPostalCode());
            ps.setString(7, address.getCountry());
            ps.setBoolean(8, address.isDefault());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) address.setAddressId(keyHolder.getKey().intValue());
        return address;
    }

    @Override
    @Transactional(readOnly = false)
    public Address update(Address address) {
        String sql = "UPDATE addresses SET street_address = ?, city = ?, state = ?, postal_code = ?, country = ?, is_default = ? WHERE address_id = ?";
        jdbcTemplate.update(sql, address.getStreetAddress(), address.getCity(), address.getState(), 
            address.getPostalCode(), address.getCountry(), address.isDefault(), address.getAddressId());
        return address;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(Integer addressId) {
        return jdbcTemplate.update("DELETE FROM addresses WHERE address_id = ?", addressId) > 0;
    }

    @Override
    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM addresses", Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public boolean existsById(Integer addressId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM addresses WHERE address_id = ?", Long.class, addressId);
        return count != null && count > 0;
    }

    public List<Address> findByUserId(int userId) {
        return jdbcTemplate.query("SELECT * FROM addresses WHERE user_id = ? ORDER BY is_default DESC, created_at DESC", 
            addressRowMapper, userId);
    }

    public Optional<Address> findDefaultAddress(int userId, AddressType type) {
        String sql = "SELECT * FROM addresses WHERE user_id = ? AND address_type = ? AND is_default = true";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, addressRowMapper, userId, type.name().toLowerCase()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Address> findDefaultAddress(int userId) {
        String sql = "SELECT * FROM addresses WHERE user_id = ? AND is_default = true";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, addressRowMapper, userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = false)
    public void clearDefaultAddresses(int userId) {
        jdbcTemplate.update("UPDATE addresses SET is_default = false WHERE user_id = ?", userId);
    }

    @Transactional(readOnly = false)
    public Address setDefaultAddress(int addressId) {
        jdbcTemplate.update("UPDATE addresses SET is_default = true WHERE address_id = ?", addressId);
        return findById(addressId).orElse(null);
    }

    @Transactional(readOnly = false)
    public void setDefaultAddress(int addressId, int userId) {
        clearDefaultAddresses(userId);
        jdbcTemplate.update("UPDATE addresses SET is_default = true WHERE address_id = ?", addressId);
    }

    public long countByUserId(int userId) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM addresses WHERE user_id = ?", Long.class, userId);
        return count != null ? count : 0L;
    }
}
