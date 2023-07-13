package com.halfacode.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShipmentDTO {

    private String shipmentId;
    private LocalDate estimatedDeliveryDate;

    private Long orderId;
}
