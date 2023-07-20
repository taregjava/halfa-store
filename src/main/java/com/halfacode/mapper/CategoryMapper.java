package com.halfacode.mapper;

import com.halfacode.dto.CategoryDTO;
import com.halfacode.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {


    public static CategoryDTO buildCategoryDTO(Category categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .imageName(categoryEntity.getImageName())
                .build();
    }
    public CategoryDTO mapEntityToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setImageName(category.getImageName());
        return categoryDTO;
    }

    public Category mapDtoToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setImageName(categoryDTO.getImageName());
        return category;
    }
}
