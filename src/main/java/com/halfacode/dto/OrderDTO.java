package com.halfacode.dto;

import com.halfacode.entity.order.PaymentMethod;
import lombok.Data;

@Data
public class OrderDTO {

    private String country;
    private PaymentMethod paymentMethod;
    private long userId;
}
