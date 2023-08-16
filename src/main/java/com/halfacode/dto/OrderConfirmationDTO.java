package com.halfacode.dto;

import com.halfacode.entity.order.OrderStatus;
import com.halfacode.entity.order.OrderTrack;
import com.halfacode.entity.order.PaymentMethod;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderConfirmationDTO {
    private Long id;
    private Date orderTime;
    private String country;
    private OrderStatus status;
  //  private PaymentMethod paymentMethod;
    private float shippingCost;
    private float productCost;
    private float subtotal;
   // private int deliverDays;
   // private Date deliverDate;
   // private List<OrderTrack> orderTracks;
   // private float productCostTotal;
    private List<String> productNames;
    // Add any other fields or methods needed for order confirmation display
}