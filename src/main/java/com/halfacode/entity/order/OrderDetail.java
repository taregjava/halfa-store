package com.halfacode.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.halfacode.entity.CartItem;
import com.halfacode.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private float productCost;
    private float shippingCost;
    private float unitPrice;
    private float subtotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne // Add this mapping to associate with CartItem
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    // Add a constructor to set the product cost, shipping cost, unit price, and subtotal
    public OrderDetail(Product product, int quantity, float unitPrice, float shippingCost) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.shippingCost = shippingCost;
        this.productCost = unitPrice * quantity;
        this.subtotal = this.productCost + this.shippingCost;
    }

    // Override toString() method for better logging and debugging
    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", productCost=" + productCost +
                ", shippingCost=" + shippingCost +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", product=" + product +
                ", order=" + order +
                ", cartItem=" + cartItem +
                '}';
    }
}