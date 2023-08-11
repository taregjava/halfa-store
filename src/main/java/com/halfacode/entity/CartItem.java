package com.halfacode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.halfacode.entity.order.Order;
import com.halfacode.entity.order.OrderDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

  /*  @OneToOne(mappedBy = "cartItem")
    private OrderDetail orderDetail;*/

    @Transient
    private float shippingCost;

    private boolean isCheckedOut;

    @OneToMany(mappedBy = "cartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    public CartItem(User user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    @Transient
    public float getSubtotal() {
        return product.getDiscountPercent() * quantity;
    }

    @Transient
    public float getShippingCost() {
        return shippingCost;
    }
}