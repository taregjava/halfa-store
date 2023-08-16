package com.halfacode.service;

import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.repoistory.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public RecommendationService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Product> getRecommendedProducts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<Long> interactedProductIds = user.getInteractedProductIds();

        List<Product> allProducts = productRepository.findAll();
        List<Product> recommendedProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (!interactedProductIds.contains(product.getId())) {
                recommendedProducts.add(product);
            }
        }

        Collections.shuffle(recommendedProducts);
        return recommendedProducts.subList(0, Math.min(recommendedProducts.size(), 10));
    }
}