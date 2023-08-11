package com.halfacode.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private T payload;
    private String message;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    public ApiResponse(T payload, String error) {
        this.status = 401; // Unauthorized status
        this.payload = payload;
        this.message = error;
        this.timestamp = LocalDateTime.now();
    }
    public ApiResponse(String error,T payload) {
        this.message = error;
        this.payload = payload;
    }
    public boolean isSuccessful() {
        return status >= 200 && status < 300;
    }
    public ApiResponse(int status, T payload, String error, LocalDateTime timestamp) {
        this.status = status;
        this.payload = payload;
        this.message = error;
        this.timestamp = timestamp;
    }
    public ApiResponse(int status, T payload, String error) {
        this(status, payload, error, LocalDateTime.now());
    }

    public ApiResponse(int status, T payload, LocalDateTime now) {
        this.status = status;
        this.payload = payload;
        this.timestamp = now;

    }
    public String getTimestampAsString() {
        if (this.timestamp == null) {
            return null;
        }
        return this.timestamp.toString();
    }

  /*  public String getTimestampAsString() {
        if (this.timestamp == null) {
            return null;
        }
        return this.timestamp.toString();
    }*/
}
