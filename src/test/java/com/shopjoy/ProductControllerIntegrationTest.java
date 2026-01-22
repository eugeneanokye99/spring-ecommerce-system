package com.shopjoy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopjoy.controller.ProductController;
import com.shopjoy.dto.ProductDTO;
import com.shopjoy.entity.Product;
import com.shopjoy.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Product testProduct;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        testProduct.setCostPrice(BigDecimal.valueOf(50.00));
        testProduct.setStockQuantity(100);

        testProductDTO = new ProductDTO();
        testProductDTO.setProductId(1L);
        testProductDTO.setProductName("Test Product");
        testProductDTO.setDescription("Test Description");
        testProductDTO.setPrice(BigDecimal.valueOf(99.99));
        testProductDTO.setStockQuantity(100);
    }

    @Test
    void createProduct_ShouldReturn201() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(testProductDTO);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value(1))
                .andExpect(jsonPath("$.data.productName").value("Test Product"));
    }

    @Test
    void getProductById_ShouldReturn200() throws Exception {
        when(productService.getProductById(1L)).thenReturn(testProductDTO);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productId").value(1))
                .andExpect(jsonPath("$.data.productName").value("Test Product"))
                .andExpect(jsonPath("$.data.price").value(99.99));
    }

    @Test
    void getAllProducts_ShouldReturn200WithList() throws Exception {
        List<ProductDTO> products = Arrays.asList(testProductDTO);
        when(productService.getAllProducts(any())).thenReturn(products);

        mockMvc.perform(get("/api/v1/products")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].productName").value("Test Product"));
    }

    @Test
    void updateProduct_ShouldReturn200() throws Exception {
        testProductDTO.setProductName("Updated Product");
        when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(testProductDTO);

        mockMvc.perform(put("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productName").value("Updated Product"));
    }

    @Test
    void deleteProduct_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturn400() throws Exception {
        ProductDTO invalidDTO = new ProductDTO();
        invalidDTO.setProductName("");
        invalidDTO.setPrice(BigDecimal.valueOf(-10));

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProductById_WithNonExistentId_ShouldReturn404() throws Exception {
        when(productService.getProductById(999L)).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void filterProductsByPriceRange_ShouldReturn200() throws Exception {
        when(productService.getProductsByPriceRange(
            any(BigDecimal.class), 
            any(BigDecimal.class), 
            any()
        )).thenReturn(Arrays.asList(testProductDTO));

        mockMvc.perform(get("/api/v1/products")
                .param("minPrice", "50")
                .param("maxPrice", "150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void searchProductsByName_ShouldReturn200() throws Exception {
        when(productService.searchProductsByName(anyString(), any()))
            .thenReturn(Arrays.asList(testProductDTO));

        mockMvc.perform(get("/api/v1/products/search")
                .param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].productName").value("Test Product"));
    }
}
