package com.halfacode.service;

import com.halfacode.dto.CartItemDTO;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.exception.UserNotFoundException;
import com.halfacode.repoistory.CartItemRepository;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.repoistory.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    /*public List<CartItem> getCartItemsByProductIdsAndUser(List<Long> productIds, User user) {
        return cartItemRepository.findByProductIdsAndUser(productIds, user);
    }*/

   /* public void addItemToCart(CartItemDTO cartItemDTO) {
        // Fetch the user based on the provided ID
        Long userId = cartItemDTO.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Extract the product IDs and quantities from the CartItemDTO
        List<Long> productIds = cartItemDTO.getProductIds();
        List<Integer> quantities = cartItemDTO.getQuantities();

        // Fetch the products based on the provided IDs
        List<Product> products = productRepository.findAllById(productIds);

        // Create or update cart items for each product and its corresponding quantity
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int quantity = quantities.get(i);

            CartItem cartItem = cartItemRepository.findCartItemByProductAndUser(product, user);

            if (cartItem == null) {
                // If the cart item doesn't exist, create a new one
                cartItem = new CartItem(user, product, quantity);
            } else {
                // If the cart item already exists, update its quantity
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }

            // Save the cart item
            cartItemRepository.save(cartItem);
        }
    }*/

    public void addItemToCart(CartItemDTO cartItemDTO) {
        User user = userRepository.findById(cartItemDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found")); // Custom exception handling if user not found
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found")); // Custom exception handling if product not found

        CartItem cartItem = new CartItem(user, product, cartItemDTO.getQuantity());

        user.getCartItems().add(cartItem);
        userRepository.save(user);
    }

    // Implement other methods for updating and removing items from the cart.

    // Implement other methods for updating and removing items from the cart.
}