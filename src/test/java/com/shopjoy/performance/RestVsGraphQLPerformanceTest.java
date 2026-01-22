package com.shopjoy.performance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestVsGraphQLPerformanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Test
    void compareSingleProductRetrieval() {
        long restTime = measureRestPerformance(() -> {
            ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/products/1", 
                String.class
            );
            return response.getStatusCodeValue() == 200;
        }, 100);

        long graphqlTime = measureGraphQLPerformance(() -> {
            graphQlTester.document("""
                query {
                    product(id: 1) {
                        productId
                        productName
                        price
                    }
                }
                """).execute().path("product").pathExists();
            return true;
        }, 100);

        System.out.println("REST Average Time: " + restTime + "ms");
        System.out.println("GraphQL Average Time: " + graphqlTime + "ms");
        System.out.println("Performance Difference: " + 
            Math.abs(restTime - graphqlTime) + "ms");
    }

    @Test
    void compareMultipleProductsRetrieval() {
        long restTime = measureRestPerformance(() -> {
            ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/products?page=0&size=20", 
                String.class
            );
            return response.getStatusCodeValue() == 200;
        }, 100);

        long graphqlTime = measureGraphQLPerformance(() -> {
            graphQlTester.document("""
                query {
                    products(page: 0, size: 20) {
                        content {
                            productId
                            productName
                            price
                        }
                    }
                }
                """).execute().path("products.content").pathExists();
            return true;
        }, 100);

        System.out.println("REST List Average Time: " + restTime + "ms");
        System.out.println("GraphQL List Average Time: " + graphqlTime + "ms");
    }

    @Test
    void compareComplexQueryPerformance() {
        long restTime = measureRestPerformance(() -> {
            restTemplate.getForEntity("/api/v1/products/1", String.class);
            restTemplate.getForEntity("/api/v1/users/1", String.class);
            restTemplate.getForEntity("/api/v1/orders/1", String.class);
            return true;
        }, 50);

        long graphqlTime = measureGraphQLPerformance(() -> {
            graphQlTester.document("""
                query {
                    product(id: 1) {
                        productId
                        productName
                        category {
                            categoryId
                            categoryName
                        }
                    }
                    user(id: 1) {
                        userId
                        username
                        orders {
                            orderId
                            totalAmount
                        }
                    }
                }
                """).execute().path("product").pathExists();
            return true;
        }, 50);

        System.out.println("REST Complex Query Time: " + restTime + "ms");
        System.out.println("GraphQL Complex Query Time: " + graphqlTime + "ms");
        
        long improvement = restTime - graphqlTime;
        double improvementPercent = (improvement * 100.0) / restTime;
        System.out.println("GraphQL Improvement: " + improvementPercent + "%");
    }

    @Test
    void compareConcurrentRequests() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(50);
        
        long restStart = System.currentTimeMillis();
        List<Future<Boolean>> restFutures = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            restFutures.add(executor.submit(() -> {
                ResponseEntity<String> response = restTemplate.getForEntity(
                    "/api/v1/products/1", 
                    String.class
                );
                return response.getStatusCodeValue() == 200;
            }));
        }
        for (Future<Boolean> future : restFutures) {
            future.get();
        }
        long restTime = System.currentTimeMillis() - restStart;

        long graphqlStart = System.currentTimeMillis();
        List<Future<Boolean>> graphqlFutures = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            graphqlFutures.add(executor.submit(() -> {
                graphQlTester.document("""
                    query {
                        product(id: 1) {
                            productId
                            productName
                        }
                    }
                    """).execute().path("product").pathExists();
                return true;
            }));
        }
        for (Future<Boolean> future : graphqlFutures) {
            future.get();
        }
        long graphqlTime = System.currentTimeMillis() - graphqlStart;

        executor.shutdown();

        System.out.println("REST Concurrent (200 requests): " + restTime + "ms");
        System.out.println("GraphQL Concurrent (200 requests): " + graphqlTime + "ms");
        System.out.println("Throughput - REST: " + (200000.0 / restTime) + " req/sec");
        System.out.println("Throughput - GraphQL: " + (200000.0 / graphqlTime) + " req/sec");
    }

    @Test
    void comparePayloadSize() {
        ResponseEntity<String> restResponse = restTemplate.getForEntity(
            "/api/v1/products?page=0&size=10", 
            String.class
        );
        int restPayloadSize = restResponse.getBody().length();

        String graphqlResponse = graphQlTester.document("""
            query {
                products(page: 0, size: 10) {
                    content {
                        productId
                        productName
                        price
                    }
                }
            }
            """).execute().path("products").entity(String.class).get();
        int graphqlPayloadSize = graphqlResponse.length();

        System.out.println("REST Payload Size: " + restPayloadSize + " bytes");
        System.out.println("GraphQL Payload Size: " + graphqlPayloadSize + " bytes");
        System.out.println("Payload Difference: " + 
            Math.abs(restPayloadSize - graphqlPayloadSize) + " bytes");
    }

    private long measureRestPerformance(Callable<Boolean> operation, int iterations) {
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            try {
                operation.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            long duration = (System.nanoTime() - start) / 1_000_000;
            times.add(duration);
        }
        return times.stream().mapToLong(Long::longValue).sum() / iterations;
    }

    private long measureGraphQLPerformance(Callable<Boolean> operation, int iterations) {
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            try {
                operation.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            long duration = (System.nanoTime() - start) / 1_000_000;
            times.add(duration);
        }
        return times.stream().mapToLong(Long::longValue).sum() / iterations;
    }
}
