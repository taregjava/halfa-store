package com.halfacode.dto;

import lombok.Data;

@Data
public class LocationRequest {
    private double latitude;
    private double longitude;

    public LocationRequest() {
    }

    public LocationRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
}