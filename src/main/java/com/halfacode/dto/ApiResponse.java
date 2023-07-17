package com.halfacode.dto;

import com.halfacode.entity.Product;
import com.halfacode.entity.Shipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private T payload;
    private String error;
    private LocalDateTime timestamp;
   // private List<String> errors;
    private boolean success;

    public ApiResponse(String error,T payload) {
        this.error = error;
        this.payload = payload;
    }

    public ApiResponse(boolean b, String product_updated_successfully, T payload) {
        this.success=b;
        this.error= product_updated_successfully;
        this.payload = payload;
    }

    public boolean isSuccessful() {
        return success;
    }
    public ApiResponse(int status, T payload, String error, LocalDateTime timestamp) {
        this.status = status;
        this.payload = payload;
        this.error = error;
        this.timestamp = timestamp;
    }
    public ApiResponse(int status, T payload, String error) {
        this.status = status;
        this.payload = payload;
        this.error = error;
        this.timestamp = timestamp;
    }

    public ApiResponse(int status, T payload, LocalDateTime now) {
        this.status = status;
        this.payload = payload;
        this.timestamp = now;

    }
}