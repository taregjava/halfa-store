package com.halfacode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halfacode.entity.Product;
import com.halfacode.entity.Shipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private T payload;
    private String error;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime timestamp;


    public ApiResponse(String error,T payload) {
        this.error = error;
        this.payload = payload;
    }

    public boolean isSuccessful() {
        return status >= 200 && status < 300;
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
    public String getTimestampAsString() {
        return "[" +
                timestamp.getYear() + "," +
                timestamp.getMonthValue() + "," +
                timestamp.getDayOfMonth() + "," +
                timestamp.getHour() + "," +
                timestamp.getMinute() + "," +
                timestamp.getSecond() + "," +
                timestamp.getNano() +
                "]";
    }
}
