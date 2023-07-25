package com.halfacode.mapper;
import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import com.halfacode.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private CategoryService categoryService;

    public static ProductDTO buildProductDTO(Product productEntity) {
        return ProductDTO.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .categoryId(productEntity.getCategoryId())
                .imageName(productEntity.getImageName())
                .fullDescription(productEntity.getFullDescription())
                .createdTime(productEntity.getCreatedTime())
                .updatedTime(productEntity.getUpdatedTime())
                .enabled(productEntity.isEnabled())
                .inStock(productEntity.isInStock())
                .cost(productEntity.getCost())
                .price(productEntity.getPrice())
                .discountPercent(productEntity.getDiscountPercent())
                .build();
    }

    public Product mapDtoToEntity(ProductDTO dto) {

        validateProductDto(dto);

        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCategory(new Category(dto.getCategoryId()));
        entity.setImageName(dto.getImageName());
        entity.setFullDescription(dto.getFullDescription());
        entity.setCreatedTime(new Date());
        entity.setUpdatedTime(new Date());
        entity.setEnabled(dto.getEnabled());
        entity.setInStock(dto.getInStock());
        entity.setCost(dto.getCost());
        entity.setPrice(dto.getPrice());
        entity.setDiscountPercent(dto.getDiscountPercent() != null ? dto.getDiscountPercent().floatValue() : 0.0f);

        return entity;
    }

    public static List<ProductDTO> mapEntityListToDtoList(List<Product> entityList) {
        return entityList.stream()
                .map(ProductMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
    public static  ProductDTO  mapEntityToDto(Product entity) {

        try {
        validateProductEntity(entity);

        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setImageName(entity.getImageName());
        dto.setFullDescription(entity.getFullDescription());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setEnabled(entity.isEnabled());
        dto.setInStock(entity.isInStock());
        dto.setCost(entity.getCost());
        dto.setPrice(entity.getPrice());
        dto.setDiscountPercent(entity.getDiscountPercent());

        return dto;
        } catch (IllegalArgumentException ex) {
            // Catch any validation errors and return an appropriate error response
            throw new RuntimeException("Validation error: " + ex.getMessage());


        }
    }

  public Product updateEntity(Product entity, ProductDTO dto) {
      // Update fields from DTO to entity if they are present in the DTO

      if (dto.getName() != null) {
          entity.setName(dto.getName());
      }
      if (dto.getImageName() != null) {
          entity.setImageName(dto.getImageName());
      }
      if (dto.getFullDescription() != null) {
          entity.setFullDescription(dto.getFullDescription());
      }
      if (dto.getUpdatedTime() != null) {
          entity.setUpdatedTime(dto.getUpdatedTime());
      }
      if (dto.getEnabled() != null) {
          entity.setEnabled(dto.getEnabled());
      }
      if (dto.getInStock() != null) {
          entity.setInStock(dto.getInStock());
      }
      if (dto.getCost() != null) {
          entity.setCost(dto.getCost());
      }
      if (dto.getPrice() != null) {
          entity.setPrice(dto.getPrice());
      }
      if (dto.getDiscountPercent() != null) {
          entity.setDiscountPercent(dto.getDiscountPercent());
      }

      // Update category if categoryId is provided
      if (dto.getCategoryId() != null) {
          ApiResponse<CategoryDTO> categoryResponse = categoryService.getCategoryById(dto.getCategoryId());
          if (categoryResponse.isSuccessful()) {
              CategoryDTO categoryDTO = categoryResponse.getPayload();
              Category category = convertToCategory(categoryDTO);
              entity.setCategory(category);
          } else {
              // Handle the unsuccessful response (e.g., logging, error handling)
          }
      }

      return entity;
  }
    public static Category convertToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        // Set other properties as needed

        return category;
    }
    private void validateProductDto(ProductDTO dto) {
        Assert.notNull(dto, "ProductDTO must not be null");
        Assert.isTrue(StringUtils.isNotBlank(dto.getName()), "Product name must not be blank");
        Assert.notNull(dto.getCategoryId(), "Category ID must not be null");
        // Add additional validation checks for the fields in ProductDTO
        // Example:
        // Assert.isTrue(dto.getCost() > 0, "Cost must be greater than zero");
        // Assert.isTrue(dto.getPrice() > 0, "Price must be greater than zero");
        // Assert.isTrue(dto.getDiscountPercent() >= 0 && dto.getDiscountPercent() <= 100, "Discount percent must be between 0 and 100");
    }

    private static void validateProductEntity(Product entity) {
        Assert.notNull(entity, "Product entity must not be null");
        Assert.isTrue(StringUtils.isNotBlank(entity.getName()), "Product name must not be blank");
        Assert.notNull(entity.getCategory(), "Category must not be null");
        // Add additional validation checks for the fields in Product entity
        // Example:
        // Assert.isTrue(entity.getCost() > 0, "Cost must be greater than zero");
        // Assert.isTrue(entity.getPrice() > 0, "Price must be greater than zero");
        // Assert.isTrue(entity.getDiscountPercent() >= 0 && entity.getDiscountPercent() <= 100, "Discount percent must be between 0 and 100");
    }


}