package com.halfacode.controller;

import com.halfacode.dto.CartItemDTO;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        // Extract the necessary information from the CartItemDTO object
        Long userId = cartItemDTO.getUserId();
        Long productId = cartItemDTO.getProductId();
        int quantity = cartItemDTO.getQuantity();

        // Call the service method to add the item to the cart
        cartService.addItemToCart(userId, productId, quantity);

        return ResponseEntity.ok("Item added to the cart");
    }

    // Implement other API endpoints for updating and removing items from the cart.
}