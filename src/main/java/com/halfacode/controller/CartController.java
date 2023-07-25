package com.halfacode.controller;

import com.halfacode.dto.CartItemDTO;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        cartService.addItemToCart(cartItemDTO);
        return ResponseEntity.ok("Items added to the cart");
    }
    // Implement other API endpoints for updating and removing items from the cart.
}