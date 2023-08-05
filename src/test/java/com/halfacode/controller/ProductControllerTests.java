package com.halfacode.controller;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.ProductDTO;
import com.halfacode.service.CategoryService;
import com.halfacode.service.ImageService;
import com.halfacode.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private CategoryService categoryService;

    // Additional setup or utility methods can be added here

    @BeforeEach
    public void setUp() {
        // Initialize any necessary mock behavior for the ProductService, ImageService, or CategoryService if needed
        // For example:

        // Mock response for getAllProducts
        List<ProductDTO> products = Arrays.asList(
                createProductDTO(1L, "Product 1", 1L, "image1.jpg", "Description 1", true, true, 10.0f, 20.0f, 5.0f),
                createProductDTO(2L, "Product 2", 2L, "image2.jpg", "Description 2", true, true, 15.0f, 25.0f, 2.0f)
        );
        ApiResponse<List<ProductDTO>> allProductsResponse = new ApiResponse<>(HttpStatus.OK.value(), products, (String) null);
        when(productService.getAllProducts()).thenReturn(allProductsResponse);

        // Mock response for getProductById
        Long productId = 1L;
        ProductDTO productDTO = createProductDTO(1L, "Product 1", 1L, "image1.jpg", "Description 1", true, true, 10.0f, 20.0f, 5.0f);
        ApiResponse<ProductDTO> productByIdResponse = new ApiResponse<>(HttpStatus.OK.value(), productDTO, (String) null);
        when(productService.getProductById(productId)).thenReturn(productByIdResponse);

        // You can continue to mock other methods and their responses as needed

        // Set up the MockMvc instance
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService, imageService, categoryService))
                .build();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        // Perform the GET request
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload[0].name").value("Product 1"))
                .andExpect(jsonPath("$.payload[1].name").value("Product 2"))
                // Add more assertions based on your expected response
                .andReturn();
    }

    @Test
    public void testGetProductById() throws Exception {
        // Perform the GET request
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.payload.name").value("Product 1"))
                // Add more assertions based on your expected response
                .andReturn();
    }

    // Helper method to create ProductDTO instances
    private ProductDTO createProductDTO(Long id, String name, Long categoryId, String imageName, String fullDescription,
                                        boolean enabled, boolean inStock, float cost, float price, float discountPercent) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setName(name);
        productDTO.setCategoryId(categoryId);
        productDTO.setImageName(imageName);
        productDTO.setFullDescription(fullDescription);
        productDTO.setEnabled(enabled);
        productDTO.setInStock(inStock);
        productDTO.setCost(cost);
        productDTO.setPrice(price);
        productDTO.setDiscountPercent(discountPercent);
        return productDTO;
    }
}