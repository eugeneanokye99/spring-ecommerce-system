package com.shopjoy.repository;

import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

/**
 * Generic Repository interface for CRUD operations using Spring JDBC.
 * <p>
 * This replaces the old GenericDAO interface with Spring-based conventions:
 * - Uses Optional<T> instead of returning null for findById
 * - Throws Spring's DataAccessException instead of SQLException
 * - Follows Spring Data naming conventions for consistency
 * <p>
 * Key differences from old GenericDAO:
 * 1. SQLException replaced with Spring's DataAccessException hierarchy
 * 2. findById returns Optional<T> for null-safety
 * 3. Methods are designed for Spring transaction management
 * 4. Exception handling is delegated to Spring's exception translation
 * <p>
 * Spring's DataAccessException hierarchy includes:
 * - DataIntegrityViolationException (constraint violations)
 * - DuplicateKeyException (unique constraint violations)
 * - EmptyResultDataAccessException (query returned no results)
 * - IncorrectResultSizeDataAccessException (expected 1, got many)
 * 
 * @param <T>  Entity type
 * @param <ID> Identifier type (e.g., Integer, Long)
 * 
 * @author ShopJoy Team
 * @since 2.0 (Spring Migration)
 */
public interface GenericRepository<T, ID> {

    /**
     * Find an entity by its identifier.
     * <p>
     * Returns Optional<T> to handle null cases explicitly and avoid NPE.
     * <p>
     * Usage:
     * Optional<User> userOpt = userRepository.findById(123);
     * if (userOpt.isPresent()) {
     *     User user = userOpt.get();
     *     // process user
     * }
     * <p>
     * Or with lambda:
     * userRepository.findById(123)
     *     .ifPresent(user -> System.out.println(user.getUsername()));
     * 
     * @param id the identifier of the entity
     * @return Optional containing the found entity, or empty Optional if not found
     * @throws DataAccessException if a database access error occurs
     */
    Optional<T> findById(ID id) throws DataAccessException;

    /**
     * Retrieve all entities of this type.
     * <p>
     * Returns empty list if no entities found (never null).
     * Consider adding pagination for large result sets.
     * 
     * @return list of all entities (maybe empty, never null)
     * @throws DataAccessException if a database access error occurs
     */
    List<T> findAll() throws DataAccessException;

    /**
     * Persist a new entity in the database.
     * <p>
     * The implementation should populate any generated identifiers (like auto-increment IDs)
     * on the passed entity and return it.
     * <p>
     * For entities with database-generated IDs:
     * - Use RETURNING clause (PostgreSQL) or GeneratedKeyHolder (other databases)
     * - Set the generated ID on the entity object
     * - Return the entity with its ID populated
     * 
     * @param entity entity to save (must not be null)
     * @return the saved entity with generated ID populated
     * @throws DataAccessException if a database access error occurs
     * @throws IllegalArgumentException if entity is null
     */
    T save(T entity) throws DataAccessException;

    /**
     * Update an existing entity in the database.
     * <p>
     * Only updates the entity if it exists. Does not create new entity.
     * Consider returning boolean or int (rows affected) to indicate success.
     * 
     * @param entity entity with updated values (must not be null, must have valid ID)
     * @return the updated entity
     * @throws DataAccessException if a database access error occurs
     * @throws IllegalArgumentException if entity is null or has no ID
     */
    T update(T entity) throws DataAccessException;

    /**
     * Delete an entity by its identifier.
     * 
     * @param id identifier of entity to delete (must not be null)
     * @return true if deletion succeeded (row existed and was deleted), false otherwise
     * @throws DataAccessException if a database access error occurs
     * @throws IllegalArgumentException if id is null
     */
    boolean delete(ID id) throws DataAccessException;

    /**
     * Count total entities of this type in the database.
     * <p>
     * Useful for pagination calculations.
     * 
     * @return the total count (0 if no entities exist)
     * @throws DataAccessException if a database access error occurs
     */
    long count() throws DataAccessException;

    /**
     * Check if an entity exists by its identifier.
     * <p>
     * More efficient than findById() when you only need existence check.
     * Typically implemented as: SELECT 1 FROM table WHERE id = ? LIMIT 1
     * 
     * @param id identifier to check
     * @return true if entity exists, false otherwise
     * @throws DataAccessException if a database access error occurs
     */
    boolean existsById(ID id) throws DataAccessException;
}
