# Integration Tests

This directory contains integration tests for the Shopjoy E-commerce System. These tests verify end-to-end functionality across the full application stack: **Controller → Service → Repository → Database**.

## Test Structure

### What are Integration Tests?

Integration tests verify that multiple components of the application work together correctly. Unlike unit tests that test individual components in isolation, integration tests:

- Load the full Spring application context using `@SpringBootTest`
- Use real database connections (PostgreSQL test database)
- Test the complete request flow through all layers
- Verify business logic, data persistence, and transaction management

### Test Files

1. **UserIntegrationTest.java**
   - Tests user registration, authentication, profile updates
   - Verifies username/email uniqueness constraints
   - Tests password hashing and verification
   - **11 test cases**

2. **ProductIntegrationTest.java**
   - Tests product CRUD operations
   - Verifies product-category relationships
   - Tests filtering by category, price range, and active status
   - **14 test cases**

3. **CategoryIntegrationTest.java**
   - Tests category hierarchy (parent-child relationships)
   - Verifies category movement and deletion constraints
   - Tests subcategory retrieval
   - **11 test cases**

4. **OrderIntegrationTest.java**
   - Tests order creation with inventory reservation
   - Verifies order status transitions (state machine)
   - Tests order-user-product-address relationships
   - **8 test cases**

## Running the Tests

### Prerequisites

1. PostgreSQL database running on `localhost:5432`
2. Test database `shopjoy_test` created
3. Database credentials configured in `application-test.properties`

### Run All Tests

```bash
# Using Maven wrapper
./mvnw test

# Or specifically run integration tests
./mvnw test -Dtest="*IntegrationTest"
```

### Run Specific Test Class

```bash
# Run only User integration tests
./mvnw test -Dtest=UserIntegrationTest

# Run only Product integration tests
./mvnw test -Dtest=ProductIntegrationTest
```

### Run with Test Profile

```bash
./mvnw test -Dspring.profiles.active=test
```

## Test Configuration

### Profile Activation

All integration tests use `@ActiveProfiles("test")` to ensure they run with the test configuration from `application-test.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/shopjoy_test
spring.datasource.username=postgres
spring.datasource.password=Final@2025
```

### Transaction Management

Tests are annotated with `@Transactional` to ensure database changes are rolled back after each test, maintaining test isolation and a clean database state.

## Test Strategy

### Integration vs Unit Tests

**Integration Tests (this directory):**
- ✅ Test full stack (controller → service → repository)
- ✅ Use real database connections
- ✅ Verify business logic AND data persistence
- ✅ Test transaction boundaries
- ❌ Slower execution time
- ❌ Require database setup

**Unit Tests (future):**
- ✅ Fast execution
- ✅ Test individual methods in isolation
- ✅ Use mocks for dependencies
- ❌ Don't verify database operations
- ❌ May miss integration issues

### Test Naming Convention

Tests follow the pattern: `test<MethodName>_<Scenario>`

Examples:
- `testCreateProduct()` - Happy path
- `testGetProductByIdNotFound()` - Error handling
- `testDuplicateUsername()` - Business rule validation

## GitHub Actions CI Pipeline

The `.github/workflows/ci.yml` file automatically runs all tests on:
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches

The CI pipeline:
1. Spins up a PostgreSQL container
2. Builds the application
3. Runs all unit tests
4. Runs all integration tests
5. Uploads test results as artifacts

## Test Coverage

Current integration test coverage:

| Component | Test Cases | Status |
|-----------|------------|--------|
| User Management | 11 | ✅ |
| Product Management | 14 | ✅ |
| Category Management | 11 | ✅ |
| Order Management | 8 | ✅ |
| Address Management | - | ⏳ Pending |
| Cart Management | - | ⏳ Pending |
| Inventory Management | - | ⏳ Pending |
| Review Management | - | ⏳ Pending |

## Adding New Integration Tests

To add new integration tests:

1. Create a new test class in this directory
2. Annotate with `@SpringBootTest` and `@ActiveProfiles("test")`
3. Add `@Transactional` for automatic rollback
4. Inject services using `@Autowired`
5. Follow the AAA pattern (Arrange, Act, Assert)

Example:

```java
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NewFeatureIntegrationTest {
    
    @Autowired
    private SomeService someService;
    
    @Test
    @DisplayName("Should do something successfully")
    void testNewFeature() {
        // Arrange
        var input = new RequestDTO();
        
        // Act
        var result = someService.doSomething(input);
        
        // Assert
        assertNotNull(result);
        assertEquals(expected, result.getValue());
    }
}
```

## Troubleshooting

### Database Connection Issues

If tests fail with database connection errors:

1. Verify PostgreSQL is running: `pg_isready -h localhost -p 5432`
2. Check test database exists: `psql -U postgres -l | grep shopjoy_test`
3. Verify credentials in `application-test.properties`

### Transaction Rollback Issues

If test data persists between runs:
- Ensure `@Transactional` is present on test class
- Check for explicit transaction commits in code
- Verify test database isolation

### Unique Constraint Violations

Tests use timestamp-based unique values to avoid conflicts:
```java
testUserRequest.setUsername("testuser_" + System.currentTimeMillis());
```

This ensures tests can run multiple times without cleanup.

## Best Practices

1. **Test Isolation**: Each test should be independent and not rely on others
2. **Use Test Profile**: Always activate the `test` profile
3. **Descriptive Names**: Use `@DisplayName` for readable test descriptions
4. **Cleanup**: Let `@Transactional` handle cleanup automatically
5. **Assertions**: Use multiple specific assertions rather than one complex assertion
6. **Test Data**: Create minimal data needed for each test
7. **Error Scenarios**: Test both happy paths and error conditions

## Resources

- [Spring Boot Testing Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [AssertJ Documentation](https://assertj.github.io/doc/)
