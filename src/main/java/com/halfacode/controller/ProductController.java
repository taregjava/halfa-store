package com.halfacode.controller;

import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import com.halfacode.service.CategoryService;
import com.halfacode.service.ImageService;
import com.halfacode.service.ProductService;
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
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestParam("file") MultipartFile imageFile, @RequestParam("name") String name, @RequestParam("categoryId") Long categoryId) throws IOException, IOException {
        String imageName = imageService.saveFile(name,imageFile);
        Category category = categoryService.getCategoryById(categoryId);
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setImageName(imageName);
        return productService.createProduct(product);
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