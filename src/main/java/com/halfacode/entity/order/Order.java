package com.halfacode.entity.order;

import com.halfacode.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonManagedReference;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_time")
    private Date orderTime;

    @Column(name = "country")
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private float shippingCost;
    private float productCost;
    private float subtotal;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    private int deliverDays;
    private Date deliverDate;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTracks = new ArrayList<>();
    // Constructors, getters, setters, etc.

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
        orderDetails.remove(orderDetail);
        orderDetail.setOrder(null);
    }

    public void addOrderTrack(OrderTrack orderTrack) {
        orderTracks.add(orderTrack);
        orderTrack.setOrder(this);
    }

    public void removeOrderTrack(OrderTrack orderTrack) {
        orderTracks.remove(orderTrack);
        orderTrack.setOrder(null);
    }

    // Calculate and return the total product cost for the order
    public float getProductCostTotal() {
        float total = 0.0f;
        for (OrderDetail orderDetail : orderDetails) {
            total += orderDetail.getProductCost() * orderDetail.getQuantity();
        }
        return total;
    }
    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;
        Order other = (Order) obj;
        return Objects.equals(id, other.id);
    }
}