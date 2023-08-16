package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import com.halfacode.entity.User;
import com.halfacode.exception.CustomException;
import com.halfacode.service.CategoryService;
import com.halfacode.service.ImageService;
import com.halfacode.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final ImageService imageService;

    private final CategoryService categoryService;

    public ProductController(ProductService productService, ImageService imageService, CategoryService categoryService) {
        this.productService = productService;
        this.imageService = imageService;
        this.categoryService = categoryService;
    }
    @GetMapping
    public ApiResponse<List<ProductDTO>> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> getProductById(@PathVariable Long id) {
        ApiResponse<ProductDTO> response = productService.getProductById(id);

        // You can further customize the response if needed
        return response;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@ModelAttribute ProductDTO productDTO, @RequestParam("imageFile") MultipartFile imageFile) {
        ApiResponse<ProductDTO> response = productService.createProduct(productDTO, imageFile);

        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDTO productDTO, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        ApiResponse<ProductDTO> response = productService.updateProduct(productId, productDTO, imageFile);

        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProducts (ProductDTO searchCriteria){

            List<ProductDTO> products = productService.searchProducts(searchCriteria);

            ApiResponse<List<ProductDTO>> response = new ApiResponse<>();
            response.setStatus(HttpStatus.OK.value());
            response.setPayload(products);
            response.setTimestamp(LocalDateTime.now());

            return ResponseEntity.ok(response);
        }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        ApiResponse<List<ProductDTO>> response = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByAllCtegory() {
        ApiResponse<List<ProductDTO>> response = productService.getProductsByAllCtegory();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/best-selling")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getBestSellingProducts() {
        ApiResponse<List<ProductDTO>> response = productService.getBestSellingProducts();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/deal-of-the-day")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getDealOfTheDayProducts() {
        ApiResponse<List<ProductDTO>> response = productService.getDealOfTheDayProducts();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}