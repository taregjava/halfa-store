package com.halfacode.service;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfacode.controller.CartController;
import com.halfacode.dto.CartItemDTO;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.repoistory.CartItemRepository;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.repoistory.UserRepository;
import com.halfacode.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfacode.config.TestConfig;
import com.halfacode.dto.CartItemDTO;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.repoistory.CartItemRepository;
import com.halfacode.repoistory.CategoryRepository;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.repoistory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(CartController.class)
@Import(TestConfig.class)
public class CartServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean // Add this line to mock the CategoryRepository
    private CategoryRepository categoryRepository;

    // Other setup or utility methods can be added here

    @Test
    @WithMockUser // This simulates an authenticated user
    public void testAddItemToCart() throws Exception {
        // Prepare test data
        Long userId = 1L;
        Long productId = 2L;
        int quantity = 3;

        CartItemDTO cartItemDTO = new CartItemDTO(userId, productId, quantity);

        User user = new User(); // Create a user instance

        Product product = new Product(); // Create a product instance
        product.setId(productId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(new CartItem());

        // Convert the CartItemDTO to JSON using the helper method
        String cartItemJson = toJson(cartItemDTO);

        // Perform the POST request
        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartItemJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // Add more assertions based on your expected behavior
    }

    // Helper method to convert object to JSON
    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}