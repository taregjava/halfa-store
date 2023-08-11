package com.halfacode.entity.order;

import com.halfacode.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@Table(name = "order_track")
public class OrderTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256)
    private String notes;

    private Date updatedTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id") // New field to represent the user who updated the order status
    private User updatedBy;

    // Constructors, getters, setters, etc.

    public OrderTrack() {
        this.updatedTime = new Date();
    }

    public OrderTrack(String notes, OrderStatus status, Order order, User updatedBy) {
        this.notes = notes;
        this.status = status;
        this.order = order;
        this.updatedBy = updatedBy;
        this.updatedTime = new Date();
    }
}