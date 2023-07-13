package com.halfacode.dto;

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
    private List<String> errors;

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
}