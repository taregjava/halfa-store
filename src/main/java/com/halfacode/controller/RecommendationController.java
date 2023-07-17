package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.repoistory.UserRepository;
import com.halfacode.service.RecommendationService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    public RecommendationController(RecommendationService recommendationService, UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }
    @GetMapping("/{userId}")
   // @ApiOperation("Get recommended products for a user")
   /* @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved recommended products", response = ApiResponse.class),
            @ApiResponse(code = 404, message = "User not found")
    })*/
    public ResponseEntity<ApiResponse<List<Product>>> getRecommendedProducts(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Product> recommendedProducts = recommendationService.getRecommendedProducts(userId);
        ApiResponse<List<Product>> response = new ApiResponse<>(200, recommendedProducts, null, LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}