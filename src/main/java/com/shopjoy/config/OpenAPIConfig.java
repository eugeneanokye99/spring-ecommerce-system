package com.shopjoy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type Open api config.
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Custom open api open api.
     *
     * @return the open api
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShopJoy - Smart E-Commerce System API")
                        .version("v1.0")
                        .description("RESTful API for e-commerce operations including user management, product catalog, categories, and order processing")
                        .contact(new Contact()
                                .name("ShopJoy Development Team")
                                .email("dev@shopjoy.com")
                                .url("https://shopjoy.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("http://test-api.shopjoy.com")
                                .description("Test Server"),
                        new Server()
                                .url("https://api.shopjoy.com")
                                .description("Production Server")
                ));
    }
}
