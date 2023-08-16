package com.halfacode.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartItemRequest {

    private List<Long> productId;
    private int quantity;
    private float shippingCost;
}
