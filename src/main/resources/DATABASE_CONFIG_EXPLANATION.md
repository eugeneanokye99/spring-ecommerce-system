# Database Configuration Explained

## Overview
This Spring Boot application uses **profile-based configuration** to manage database settings across different environments (development, testing, production). The configuration follows Spring Boot's externalized configuration model using `.properties` files.

---

## Configuration Files Structure

### 1. `application.properties` (Main Configuration)
**Purpose:** Contains base/common configuration shared across all environments.

```properties
# Application
spring.application.name=shopjoy-ecommerce-system
server.port=8080

# Active Profile
spring.profiles.active=dev

# HikariCP Connection Pool Settings (Applied in all profiles unless overridden)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.pool-name=ShopJoyHikariCP

# JDBC Template Settings
spring.jdbc.template.query-timeout=30
spring.jdbc.template.fetch-size=100

# GraphQL
spring.graphql.graphiql.enabled=true
spring.graphql.graphiql.path=/graphql

# OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

#### Key Properties Explained:

- **`spring.application.name=shopjoy-ecommerce-system`**
  - **What:** Application identifier used by Spring Cloud, monitoring tools, and logging
  - **Why:** Essential for microservices architecture, service discovery, and log aggregation

- **`server.port=8080`**
  - **What:** HTTP port the embedded Tomcat server listens on
  - **Why:** Standard port for development; can be overridden via environment variables in production

- **`spring.profiles.active=dev`**
  - **What:** Activates the `dev` profile by default
  - **Why:** Spring will load `application-dev.properties` automatically, allowing environment-specific configs without code changes

#### HikariCP Connection Pool (Default Settings):

**HikariCP** is the default JDBC connection pool in Spring Boot (since 2.x), chosen for:
- **Performance:** Fastest connection pool benchmarked (microsecond overhead)
- **Reliability:** Production-tested by major companies
- **Simplicity:** Zero-dependency, bytecode-level optimization

**Properties:**

- **`maximum-pool-size=10`**
  - **What:** Maximum number of database connections in the pool
  - **Why:** Limits resource consumption; formula: `connections = ((core_count * 2) + effective_spindle_count)` per PostgreSQL best practices
  - **Default of 10:** Suitable for small-to-medium applications

- **`minimum-idle=5`**
  - **What:** Minimum number of idle connections HikariCP maintains
  - **Why:** Pre-warmed connections reduce latency for incoming requests
  - **Recommendation:** Set to ~50% of maximum-pool-size

- **`connection-timeout=30000` (30 seconds)**
  - **What:** Max milliseconds a client will wait for a connection from pool
  - **Why:** Prevents indefinite waiting if pool exhausted; app fails fast with clear error
  - **Trade-off:** Too low = premature failures; too high = slow error detection

- **`idle-timeout=600000` (10 minutes)**
  - **What:** Max time a connection can sit idle before being removed
  - **Why:** Prevents resource waste; idle connections consume DB resources
  - **Note:** Only applies if connections exceed `minimum-idle`

- **`max-lifetime=1800000` (30 minutes)**
  - **What:** Maximum lifespan of a connection in the pool
  - **Why:** Forces periodic connection refresh to avoid stale connections, network issues, or DB-side timeouts
  - **Important:** Must be shorter than database's `wait_timeout` (MySQL) or `idle_in_transaction_session_timeout` (PostgreSQL)

- **`pool-name=ShopJoyHikariCP`**
  - **What:** Name appearing in logs and JMX monitoring
  - **Why:** Easier debugging in multi-datasource applications

#### JDBC Template Settings:

- **`query-timeout=30` (seconds)**
  - **What:** Max execution time for SQL queries
  - **Why:** Prevents long-running queries from blocking threads
  - **Best Practice:** Set based on 95th percentile query execution time

- **`fetch-size=100`**
  - **What:** Number of rows fetched per database round-trip
  - **Why:** Balances memory usage vs. network overhead
  - **Trade-off:** Too small = multiple round-trips; too large = high memory consumption

---

### 2. `application-dev.properties` (Development Environment)

```properties
# Database Connection (Development)
spring.datasource.url=jdbc:postgresql://localhost:5432/shopjoy_db
spring.datasource.username=postgres
spring.datasource.password=Final@2025
spring.datasource.driver-class-name=org.postgresql.Driver

# HikariCP Development Settings (Override defaults for development)
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=20000

# SQL Logging for Development
logging.level.com.shopjoy=DEBUG
logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG
logging.level.org.springframework.jdbc.core.StatementCreatorUtils=TRACE
logging.level.com.zaxxer.hikari=DEBUG
```

#### Database Connection (PostgreSQL):

- **`spring.datasource.url=jdbc:postgresql://localhost:5432/shopjoy_db`**
  - **Format Breakdown:**
    - `jdbc:` - Java Database Connectivity protocol
    - `postgresql://` - PostgreSQL driver protocol
    - `localhost:5432` - Host and port (5432 is PostgreSQL default)
    - `/shopjoy_db` - Database name
  - **Why PostgreSQL?**
    - ACID compliance (essential for e-commerce transactions)
    - Advanced indexing (B-tree, GiST, GIN for full-text search)
    - JSON/JSONB support (product attributes, metadata)
    - Strong community, mature tooling

- **`spring.datasource.username` & `password`**
  - **Security Note:** ⚠️ Hardcoded credentials acceptable ONLY in development
  - **Production:** Use environment variables or secret management (Vault, AWS Secrets Manager)

- **`spring.datasource.driver-class-name=org.postgresql.Driver`**
  - **What:** JDBC driver class for PostgreSQL
  - **Note:** Spring Boot auto-detects this from URL, but explicit declaration prevents ambiguity

#### Development-Specific Pool Settings:

- **`maximum-pool-size=5` (Reduced from 10)**
  - **Why:** Developer laptops have limited resources; smaller pool sufficient for single-user testing
  
- **`minimum-idle=2` (Reduced from 5)**
  - **Why:** Lower baseline connection overhead

- **`connection-timeout=20000` (20 seconds, reduced from 30)**
  - **Why:** Developers need faster feedback on connection issues

#### Development Logging (Verbose):

- **`logging.level.com.shopjoy=DEBUG`**
  - **What:** Application-level debug logs
  - **Shows:** Business logic flow, service method calls, validation errors

- **`logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG`**
  - **What:** Logs all SQL statements executed
  - **Shows:** Prepared statements, parameter values, result set mappings
  - **Example Output:**
    ```
    Executing prepared SQL statement [SELECT * FROM users WHERE user_id = ?]
    ```

- **`logging.level.org.springframework.jdbc.core.StatementCreatorUtils=TRACE`**
  - **What:** Extremely detailed parameter binding logs
  - **Shows:** Type conversions, null handling, parameter positions
  - **Use Case:** Debugging SQL injection issues or type mismatches

- **`logging.level.com.zaxxer.hikari=DEBUG`**
  - **What:** HikariCP internal operations
  - **Shows:** Connection acquisition/release, pool state, timeout warnings
  - **Critical For:** Diagnosing connection pool exhaustion

---

### 3. `application-prod.properties` (Production Environment)

```properties
# Production Database (externalized credentials)
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/shopjoy_prod}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# HikariCP Production Settings (Optimized for production workload)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# Production settings
logging.level.com.shopjoy=WARN
logging.level.org.springframework.jdbc=WARN
logging.level.com.zaxxer.hikari=INFO
```

#### Externalized Configuration (Security Best Practice):

- **`${DB_URL:jdbc:postgresql://localhost:5432/shopjoy_prod}`**
  - **Syntax:** `${ENVIRONMENT_VARIABLE:default_value}`
  - **How It Works:**
    1. Spring checks for `DB_URL` environment variable
    2. If not found, uses default value after colon
  - **Deployment:** Set environment variables via:
    - Docker: `-e DB_URL=...`
    - Kubernetes: ConfigMaps/Secrets
    - AWS: Parameter Store/Secrets Manager
    - Systemd: `Environment="DB_URL=..."`
  
- **`${DB_PASSWORD}` (No Default)**
  - **Security:** Application FAILS to start if password not provided
  - **Why:** Prevents accidental production deployments with insecure defaults
  - **12-Factor App Principle:** Credentials never in codebase

#### Production Pool Sizing:

- **`maximum-pool-size=20` (Doubled from dev)**
  - **Formula:** `(CPU cores * 2) + disk spindles`
    - Example: 8-core server + 4 SSDs = ~20 connections
  - **Reality Check:** Monitor actual usage; over-provisioning wastes DB resources

- **`minimum-idle=10` (50% of max)**
  - **Why:** Production traffic is consistent; pre-warming reduces latency spikes

- **Connection Timeouts (Same as defaults)**
  - **Why:** Production inherits stable base configuration; no need to override

#### Production Logging (Minimal):

- **`logging.level.com.shopjoy=WARN`**
  - **What:** Only warnings and errors logged
  - **Why:** Reduces log volume (cost savings in cloud environments), improves performance

- **`logging.level.org.springframework.jdbc=WARN`**
  - **What:** No SQL statement logging
  - **Why:** 
    - Security: Prevents logging sensitive data (passwords, credit cards in SQL)
    - Performance: Logging overhead can slow down high-throughput systems

- **`logging.level.com.zaxxer.hikari=INFO`**
  - **What:** Informational pool metrics only
  - **Shows:** Pool initialization, fatal errors
  - **Why:** Balance between visibility and noise

---

### 4. `application-test.properties` (Testing Environment)

```properties
# Test Database (use H2 or separate test DB)
spring.datasource.url=jdbc:postgresql://localhost:5432/shopjoy_test
spring.datasource.username=postgres
spring.datasource.password=Final@2025
spring.datasource.driver-class-name=org.postgresql.Driver

# HikariCP Test Settings (Minimal pool for testing)
spring.datasource.hikari.maximum-pool-size=3
spring.datasource.hikari.minimum-idle=1
spring.datasource.hikari.connection-timeout=10000

# Test Logging
logging.level.com.shopjoy=INFO
logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG
```

#### Test Database Strategy:

- **Separate Database (`shopjoy_test`)**
  - **Why:** Isolates test data from development data
  - **Best Practice:** Drop and recreate schema before each test suite
  - **Alternative:** Use H2 in-memory database for faster tests
    ```properties
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driver-class-name=org.h2.Driver
    ```

#### Minimal Connection Pool:

- **`maximum-pool-size=3`**
  - **Why:** Tests typically run sequentially; small pool sufficient
  - **Benefit:** Faster test startup, lower resource usage in CI/CD pipelines

- **`minimum-idle=1`**
  - **Why:** Single connection sufficient for most unit tests

- **`connection-timeout=10000` (10 seconds)**
  - **Why:** Faster failure detection in test environment

#### Test Logging (Balanced):

- **`logging.level.com.shopjoy=INFO`**
  - **What:** Standard informational logs
  - **Why:** Not as verbose as DEBUG, but shows test flow

- **`logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG`**
  - **Why:** Tests often validate SQL correctness; SQL logging is crucial

---

## Why `.properties` Format? (vs YAML/JSON/XML)

### Advantages of `.properties`:

1. **Simplicity**
   - Key-value pairs: `key=value`
   - No indentation sensitivity (unlike YAML)
   - Forgiving syntax (no strict structure)

2. **Spring Boot Native Support**
   - First-class support since Spring Framework 1.0
   - Automatic type conversion (`String` → `int`, `boolean`, etc.)
   - Property placeholder resolution (`${variable}`)

3. **IDE Support**
   - Autocomplete in IntelliJ IDEA, Eclipse, VS Code
   - Validation for Spring Boot properties
   - Quick navigation to property definitions

4. **Environment Variable Override**
   - Easy mapping: `spring.datasource.url` → `SPRING_DATASOURCE_URL`
   - Works seamlessly with Docker, Kubernetes, systemd

5. **No Dependencies**
   - Java `Properties` class built-in since Java 1.0
   - No need for YAML parsers (SnakeYAML)

### When to Use YAML Instead:

```yaml
# YAML is better for hierarchical/nested configs
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shopjoy_db
    username: postgres
    password: Final@2025
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
```

**YAML Pros:**
- More readable for deeply nested properties
- Supports lists/arrays easily
- Better for complex configuration

**YAML Cons:**
- Indentation errors break parsing
- Not as universally supported outside Spring
- Slightly slower parsing

---

## Configuration Loading Order (Spring Boot)

Spring Boot loads configurations in this priority (highest to lowest):

1. **Command-line arguments:** `--spring.datasource.password=secret`
2. **Java System Properties:** `-Dspring.datasource.password=secret`
3. **OS Environment Variables:** `SPRING_DATASOURCE_PASSWORD=secret`
4. **Profile-specific properties:** `application-{profile}.properties`
5. **Default properties:** `application.properties`
6. **`@Configuration` classes with `@PropertySource`**

**Example Override:**
```bash
# Start app with production profile but override port
java -jar app.jar --spring.profiles.active=prod --server.port=9090
```

---

## Database URL Format Deep Dive

### PostgreSQL JDBC URL Anatomy:

```
jdbc:postgresql://[host]:[port]/[database]?[parameters]
```

**Common Parameters:**

```properties
# SSL Connection
spring.datasource.url=jdbc:postgresql://db.example.com:5432/shopjoy?ssl=true&sslmode=require

# Connection Pooling Hints
spring.datasource.url=jdbc:postgresql://localhost:5432/shopjoy?prepareThreshold=3&preparedStatementCacheQueries=256

# Schema Selection
spring.datasource.url=jdbc:postgresql://localhost:5432/shopjoy?currentSchema=ecommerce

# Read-Only Replica
spring.datasource.url=jdbc:postgresql://replica.example.com:5432/shopjoy?readOnly=true
```

---

## Connection Pool Sizing Best Practices

### Formula (Rough Estimate):
```
Pool Size = (Number of CPU Cores × 2) + Number of Disks
```

### Example Sizing:

| Environment | CPU Cores | Disks | Formula | Recommended Pool |
|-------------|-----------|-------|---------|------------------|
| Laptop (Dev) | 4 | 1 SSD | (4×2)+1 | 5-9 |
| Production Server | 8 | 4 SSD | (8×2)+4 | 20 |
| High-Traffic Server | 16 | 8 SSD | (16×2)+8 | 40 |

### Over-Provisioning Risks:
- **Database Side:** PostgreSQL has `max_connections` (default 100); exceeding causes rejections
- **Application Side:** Too many idle connections waste memory (~5MB per connection)
- **Performance:** Context switching overhead when threads > CPU cores

### Under-Provisioning Symptoms:
```
java.sql.SQLTransientConnectionException: HikariPool-1 - Connection is not available, 
request timed out after 30000ms.
```

---

## Security Considerations

### ⚠️ Current Issues:

1. **Hardcoded Credentials in Dev/Test**
   - **Risk:** LOW (local development only)
   - **Mitigation:** `.gitignore` includes `application-dev.properties`, but still visible in repo history

2. **No Encryption at Rest**
   - **Risk:** MEDIUM (database files accessible if server compromised)
   - **Solution:** Enable PostgreSQL Transparent Data Encryption (TDE)

3. **No SSL/TLS Configuration**
   - **Risk:** HIGH (credentials sent in plaintext over network)
   - **Solution:** Add to production:
     ```properties
     spring.datasource.url=jdbc:postgresql://db.prod.com:5432/shopjoy?ssl=true&sslmode=require
     ```

### Production Security Checklist:

- [ ] Use environment variables for ALL credentials
- [ ] Enable SSL/TLS for database connections
- [ ] Rotate passwords every 90 days
- [ ] Use dedicated database users per environment (not `postgres` superuser)
- [ ] Grant minimal permissions (principle of least privilege)
- [ ] Enable database audit logging
- [ ] Use connection string obfuscation in logs
- [ ] Implement secrets rotation (AWS Secrets Manager, HashiCorp Vault)

---

## Monitoring & Troubleshooting

### HikariCP Metrics (via JMX or Micrometer):

```properties
# Enable metrics
management.endpoints.web.exposure.include=health,metrics,hikaricp
management.metrics.enable.hikari=true
```

**Key Metrics to Watch:**

1. **`hikaricp.connections.active`**
   - Active connections in use
   - Alert if consistently near `maximum-pool-size`

2. **`hikaricp.connections.idle`**
   - Idle connections waiting
   - If always 0 → pool too small

3. **`hikaricp.connections.pending`**
   - Threads waiting for connections
   - Should be 0; if not → pool exhausted

4. **`hikaricp.connections.timeout`**
   - Failed connection acquisitions
   - Critical metric; investigate immediately

### Common Issues:

**Problem:** Application hangs on startup
```
Caused by: java.net.ConnectException: Connection refused
```
**Solution:** 
- Database not running: `sudo systemctl start postgresql`
- Wrong host/port in `spring.datasource.url`
- Firewall blocking port 5432

**Problem:** Connection pool exhaustion
```
HikariPool-1 - Connection is not available, request timed out after 30000ms
```
**Solutions:**
1. Increase `maximum-pool-size`
2. Check for connection leaks (unclosed connections)
3. Increase `connection-timeout` (if temporary spike)
4. Investigate slow queries (use `EXPLAIN ANALYZE`)

**Problem:** Stale connections
```
org.postgresql.util.PSQLException: This connection has been closed
```
**Solutions:**
1. Reduce `max-lifetime` to refresh connections more frequently
2. Enable `spring.datasource.hikari.connection-test-query=SELECT 1` (not recommended; use validation timeout instead)
3. Check database `idle_in_transaction_session_timeout` setting

---

## Migration Recommendations

### 1. Move to YAML (Optional)

Convert to `application.yml` for better readability:

```yaml
spring:
  application:
    name: shopjoy-ecommerce-system
  profiles:
    active: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/shopjoy_db
    username: postgres
    password: ${DB_PASSWORD:Final@2025}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      pool-name: ShopJoyHikariCP
```

### 2. Externalize ALL Secrets

Create `.env` file (never commit):
```bash
DB_URL=jdbc:postgresql://localhost:5432/shopjoy_db
DB_USERNAME=postgres
DB_PASSWORD=Final@2025
```

Load with Spring Boot:
```bash
export $(cat .env | xargs) && java -jar app.jar
```

### 3. Add Health Checks

```properties
management.endpoint.health.show-details=always
management.health.db.enabled=true
```

Access: `http://localhost:8080/actuator/health`

### 4. Connection Validation

```properties
# Test connection before giving to application
spring.datasource.hikari.connection-test-query=SELECT 1
spring.datasource.hikari.validation-timeout=3000
```

---

## Summary

This configuration uses **Spring Boot profiles** to manage database settings across environments:

- **`application.properties`**: Base configuration with sensible defaults
- **`application-dev.properties`**: Local development with verbose logging
- **`application-prod.properties`**: Production-optimized with externalized secrets
- **`application-test.properties`**: Isolated testing environment

**Key Takeaways:**
1. **HikariCP** provides high-performance connection pooling
2. **Profile-based config** enables environment-specific tuning without code changes
3. **Externalized credentials** (via environment variables) are MANDATORY for production
4. **Connection pool sizing** should be based on CPU cores, workload, and database limits
5. **Logging levels** should be verbose in dev, minimal in production

**Next Steps:**
- [ ] Add SSL/TLS to production database connections
- [ ] Implement secrets management (Vault, AWS Secrets Manager)
- [ ] Set up connection pool monitoring (Micrometer + Prometheus)
- [ ] Create database initialization scripts (`schema.sql`, `data.sql`)
- [ ] Document database migration strategy (Flyway or Liquibase)
