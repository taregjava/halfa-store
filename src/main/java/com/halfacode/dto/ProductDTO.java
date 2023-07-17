package com.halfacode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private boolean enabled;
    private boolean inStock;
    private float cost;
    private float price;
    private float discountPercent;
}
