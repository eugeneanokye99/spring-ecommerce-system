package com.shopjoy.performance;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConnectionPoolTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConnectionPoolConfiguration() {
        assertThat(dataSource).isInstanceOf(HikariDataSource.class);
        HikariDataSource hikari = (HikariDataSource) dataSource;
        
        assertThat(hikari.getMaximumPoolSize()).isGreaterThan(0);
        assertThat(hikari.getMinimumIdle()).isGreaterThan(0);
        assertThat(hikari.getConnectionTimeout()).isGreaterThan(0);
    }

    @Test
    void testConcurrentConnectionAcquisition() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Long> acquisitionTimes = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    long start = System.currentTimeMillis();
                    try (Connection conn = dataSource.getConnection()) {
                        conn.createStatement().execute("SELECT 1");
                        long duration = System.currentTimeMillis() - start;
                        synchronized (acquisitionTimes) {
                            acquisitionTimes.add(duration);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        long averageTime = acquisitionTimes.stream()
            .mapToLong(Long::longValue)
            .sum() / acquisitionTimes.size();

        assertThat(averageTime).isLessThan(500);
        assertThat(acquisitionTimes).hasSize(threadCount);
    }

    @Test
    void testConnectionLeakPrevention() throws SQLException {
        List<Connection> connections = new ArrayList<>();
        
        try {
            for (int i = 0; i < 10; i++) {
                Connection conn = dataSource.getConnection();
                assertThat(conn.isClosed()).isFalse();
                connections.add(conn);
            }
        } finally {
            for (Connection conn : connections) {
                conn.close();
            }
        }

        Connection newConn = dataSource.getConnection();
        assertThat(newConn.isClosed()).isFalse();
        newConn.close();
    }

    @Test
    void testConnectionTimeout() {
        HikariDataSource hikari = (HikariDataSource) dataSource;
        long timeout = hikari.getConnectionTimeout();
        
        assertThat(timeout).isGreaterThan(0);
        assertThat(timeout).isLessThanOrEqualTo(30000);
    }

    @Test
    void testMaxConnectionPoolSize() throws InterruptedException {
        HikariDataSource hikari = (HikariDataSource) dataSource;
        int maxPoolSize = hikari.getMaximumPoolSize();

        ExecutorService executor = Executors.newFixedThreadPool(maxPoolSize + 10);
        CountDownLatch latch = new CountDownLatch(maxPoolSize + 10);
        List<Boolean> successfulConnections = new ArrayList<>();

        for (int i = 0; i < maxPoolSize + 10; i++) {
            executor.submit(() -> {
                try (Connection conn = dataSource.getConnection()) {
                    synchronized (successfulConnections) {
                        successfulConnections.add(true);
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    synchronized (successfulConnections) {
                        successfulConnections.add(false);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        long successful = successfulConnections.stream().filter(b -> b).count();
        assertThat(successful).isGreaterThan(0);
    }

    @Test
    void testConnectionValidity() throws SQLException {
        Connection conn = dataSource.getConnection();
        
        assertThat(conn.isValid(5)).isTrue();
        assertThat(conn.isClosed()).isFalse();
        
        conn.close();
        assertThat(conn.isClosed()).isTrue();
    }
}
