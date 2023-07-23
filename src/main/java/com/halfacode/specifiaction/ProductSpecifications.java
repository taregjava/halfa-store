package com.halfacode.specifiaction;
import com.halfacode.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ProductSpecifications {

    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> hasImageName(String imageName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("imageName"), imageName);
    }

    public static Specification<Product> hasFullDescription(String fullDescription) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fullDescription"), fullDescription);
    }

    public static Specification<Product> isEnabled(boolean enabled) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("enabled"), enabled);
    }

    public static Specification<Product> isInStock(boolean inStock) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("inStock"), inStock);
    }

    public static Specification<Product> hasCost(float cost) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("cost"), cost);
    }

    public static Specification<Product> hasPrice(float price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("price"), price);
    }

    public static Specification<Product> hasDiscountPercent(float discountPercent) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("discountPercent"), discountPercent);
    }

    public static Specification<Product> createdAfter(Date date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("createdTime"), date);
    }

    public static Specification<Product> createdBefore(Date date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("createdTime"), date);
    }

    public static Specification<Product> updatedAfter(Date date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("updatedTime"), date);
    }

    public static Specification<Product> updatedBefore(Date date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("updatedTime"), date);
    }
}