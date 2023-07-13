package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import com.halfacode.exception.CustomException;
import com.halfacode.service.CategoryService;
import com.halfacode.service.ImageService;
import com.halfacode.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public List<ProductDTO> getAllProducts() {
        ApiResponse<List<ProductDTO>> response = productService.getAllProducts();

        if (response.getStatus() == HttpStatus.OK.value()) {
            return response.getPayload();
        } else {
            throw new CustomException(response.getError());
        }
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
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}