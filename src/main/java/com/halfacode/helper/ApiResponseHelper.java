package com.halfacode.helper;
import com.halfacode.dto.ApiResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseHelper {

    public static <T> ApiResponse<T> success(T payload, String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), payload, message, LocalDateTime.now());
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), null, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, message, LocalDateTime.now());
    }

    public static ApiResponse<Void> internalServerError(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, message, LocalDateTime.now());
    }

    public static ApiResponse<Void> error(int status, String message) {
        return new ApiResponse<>(status, null, message, LocalDateTime.now());
    }
}