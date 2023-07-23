package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.entity.Category;
import com.halfacode.exception.NotFoundException;
import com.halfacode.mapper.CategoryMapper;
import com.halfacode.repoistory.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final S3Service imageService;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, S3Service imageService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.imageService = imageService;
    }


    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(String name, MultipartFile imageFile) {
        try {
            // Upload the image file to S3
            ApiResponse<String> imageResponse = imageService.uploadFile("categories", name, imageFile.getInputStream());
            if (!imageResponse.isSuccessful()) {
                return ResponseEntity.status(imageResponse.getStatus()).body(
                        new ApiResponse<>(imageResponse.getStatus(), null, imageResponse.getError(), LocalDateTime.now())
                );
            }

            String imageName = imageResponse.getPayload();

            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setName(name);

            Category category = categoryMapper.mapDtoToEntity(categoryDTO);
            category.setImageName(imageName);

            Category createdCategory = categoryRepository.save(category);
            CategoryDTO createdCategoryDTO = categoryMapper.mapEntityToDto(createdCategory);
            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.OK.value(), createdCategoryDTO, "Category created successfully", LocalDateTime.now())
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while creating the category", LocalDateTime.now())
            );
        }
    }

    public ApiResponse<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(CategoryMapper::buildCategoryDTO) // Using the static method from CategoryMapper
                .collect(Collectors.toList());

        return new ApiResponse<>(HttpStatus.OK.value(), categoryDTOs, null, LocalDateTime.now());
    }

    public ApiResponse<CategoryDTO> getCategoryById(Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            CategoryDTO categoryDTO = CategoryMapper.buildCategoryDTO(category); // Use the static method

            return new ApiResponse<>(HttpStatus.OK.value(), categoryDTO, null, LocalDateTime.now());
        } catch (NotFoundException ex) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, ex.getMessage(), LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving the category", LocalDateTime.now());
        }
    }
   /* public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
*/
   public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(MultipartFile imageFile, CategoryDTO categoryDTO) {
       try {
           // Check if the category with the given ID exists
           Optional<Category> optionalCategory = categoryRepository.findById(categoryDTO.getId());
           if (optionalCategory.isEmpty()) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                       new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, "Category not found", LocalDateTime.now())
               );
           }

           Category existingCategory = optionalCategory.get();

           // Update the category fields
           existingCategory.setName(categoryDTO.getName());

           // Upload the new image to S3 if provided
           if (imageFile != null && !imageFile.isEmpty()) {
               ApiResponse<String> imageResponse = imageService.uploadFile("categories", categoryDTO.getName(), imageFile.getInputStream());
               if (imageResponse.isSuccessful()) {
                   existingCategory.setImageName(imageResponse.getPayload());
               } else {
                   return ResponseEntity.status(imageResponse.getStatus()).body(
                           new ApiResponse<>(imageResponse.getStatus(), null, imageResponse.getError(), LocalDateTime.now())
                   );
               }
           }

           Category updatedCategory = categoryRepository.save(existingCategory);
           CategoryDTO updatedCategoryDTO = categoryMapper.mapEntityToDto(updatedCategory);
           return ResponseEntity.ok(
                   new ApiResponse<>(HttpStatus.OK.value(), updatedCategoryDTO, "Category updated successfully", LocalDateTime.now())
           );
       } catch (Exception ex) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                   new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while updating the category", LocalDateTime.now())
           );
       }
   }


    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}