package com.halfacode.dto;

import com.halfacode.entity.Address;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.User;
import com.halfacode.entity.order.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private Long orderId; // New field to specify the existing order's ID
    private Long userId;
    private Address address;
    private String country;
    private List<Long> productIds; // This represents the array of product IDs
    private int quantity;
    private float shippingCost;
    private PaymentMethod paymentMethod;
    private CheckoutInfo checkoutInfo;
}
