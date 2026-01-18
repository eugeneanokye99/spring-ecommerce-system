package com.shopjoy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic application context test
 * Ensures the Spring application context loads successfully with test profile
 */
@SpringBootTest
@ActiveProfiles("test")
class ShopjoyEcommerceSystemApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring application context loads successfully
		assertTrue(true, "Application context should load without errors");
	}

}
