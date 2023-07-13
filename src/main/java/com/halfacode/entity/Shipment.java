package com.halfacode.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;


@Entity
@Data
@ToString
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shipmentId;
    private LocalDate estimatedDeliveryDate;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "order_id")

    private Order order;
    public Shipment(){

    }
    public Shipment(String shipmentId, LocalDate estimatedDeliveryDate, Order order) {
        this.shipmentId = shipmentId;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.order = order;
    }

}
