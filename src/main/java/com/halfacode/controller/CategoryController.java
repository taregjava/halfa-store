package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.entity.Category;
import com.halfacode.service.CategoryService;
import com.halfacode.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ImageService imageService;

    public CategoryController(CategoryService categoryService, ImageService imageService) {
        this.categoryService = categoryService;
        this.imageService = imageService;
    }
    @GetMapping
    public ApiResponse<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@ModelAttribute CategoryDTO categoryDTO,
                                                                   @RequestParam("file") MultipartFile imageFile) {
        return categoryService.createCategory(categoryDTO, imageFile);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long categoryId,
                                                                   @RequestParam("imageFile") MultipartFile imageFile,
                                                                   @ModelAttribute CategoryDTO categoryDTO) {
        categoryDTO.setId(categoryId);
        return categoryService.updateCategory(imageFile, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        ApiResponse<Void> response = categoryService.deleteCategory(id);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
