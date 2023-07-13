package com.halfacode.mapper;
import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product mapDtoToEntity(ProductDTO dto) {

        validateProductDto(dto);

        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCategory(new Category(dto.getCategoryId()));
        entity.setImageName(dto.getImageName());
        entity.setFullDescription(dto.getFullDescription());
        entity.setCreatedTime(dto.getCreatedTime());
        entity.setUpdatedTime(dto.getUpdatedTime());
        entity.setEnabled(dto.isEnabled());
        entity.setInStock(dto.isInStock());
        entity.setCost(dto.getCost());
        entity.setPrice(dto.getPrice());
        entity.setDiscountPercent(dto.getDiscountPercent());

        return entity;
    }

    public static List<ProductDTO> mapEntityListToDtoList(List<Product> entityList) {
        return entityList.stream()
                .map(ProductMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
    public static  ProductDTO  mapEntityToDto(Product entity) {

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