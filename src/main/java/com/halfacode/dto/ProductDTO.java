package com.halfacode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private String imageName;
    private String fullDescription;
    private Date createdTime;
    private Date updatedTime;
    private Boolean enabled; // Use Boolean wrapper class instead of boolean primitive
    private Boolean inStock; // Use Boolean wrapper class instead of boolean primitive
    private Float cost; // Use Float wrapper class instead of float primitive
    private Float price; // Use Float wrapper class instead of float primitive
    private Float discountPercent; // Use Float wrapper class instead of float primitive


    private List<String> imageUrls;
}
