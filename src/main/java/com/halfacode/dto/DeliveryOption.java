package com.halfacode.dto;

import lombok.Data;

@Data
public class DeliveryOption {
    private String name;
    private String description;
    private double price;

    public DeliveryOption() {
    }

    public DeliveryOption(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters and setters
}