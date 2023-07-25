package com.halfacode.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long userId;
    private Long productId;
    private int quantity;
}