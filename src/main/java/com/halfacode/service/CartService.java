package com.halfacode.service;

import com.halfacode.entity.CartItem;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.repoistory.CartItemRepository;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.repoistory.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public void addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found")); // Custom exception handling if user not found
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found")); // Custom exception handling if product not found

        CartItem cartItem = new CartItem(user, product, quantity);

        user.getCartItems().add(cartItem);
        userRepository.save(user);
    }

    // Implement other methods for updating and removing items from the cart.
}