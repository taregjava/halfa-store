package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.entity.Category;
import com.halfacode.exception.NotFoundException;
import com.halfacode.mapper.CategoryMapper;
import com.halfacode.repoistory.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ApiResponse<CategoryDTO> getCategoryById(Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            CategoryDTO categoryDTO = categoryMapper.mapEntityToDto(category);

            return new ApiResponse<>(HttpStatus.OK.value(), categoryDTO, "Category retrieved successfully", LocalDateTime.now());
        } catch (NotFoundException ex) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, ex.getMessage(), LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving the category", LocalDateTime.now());
        }
    }
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}