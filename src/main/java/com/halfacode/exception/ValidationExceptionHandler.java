package com.halfacode.exception;

import com.halfacode.dto.ApiResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError(ex.getErrorMessage());
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
            BindingResult bindingResult = ex.getBindingResult();
            List<String> errors = new ArrayList<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.add(fieldError.getDefaultMessage());
            }

            ApiResponse<Void> apiResponse = new ApiResponse<>();
            apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            apiResponse.setError("Validation error");
            apiResponse.setTimestamp(LocalDateTime.now());
            apiResponse.setPayload(null);
            apiResponse.setErrors(errors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        @ExceptionHandler(BindException.class)
        public ResponseEntity<ApiResponse<Void>> handleBindExceptions(BindException ex) {
            BindingResult bindingResult = ex.getBindingResult();
            List<String> errors = new ArrayList<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.add(fieldError.getDefaultMessage());
            }

            ApiResponse<Void> apiResponse = new ApiResponse<>();
            apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            apiResponse.setError("Validation error");
            apiResponse.setTimestamp(LocalDateTime.now());
            apiResponse.setPayload(null);
            apiResponse.setErrors(errors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex) {
            ApiResponse<Void> apiResponse = new ApiResponse<>();
            apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            apiResponse.setError("Validation error");
            apiResponse.setTimestamp(LocalDateTime.now());
            apiResponse.setPayload(null);
            apiResponse.getErrors().add(ex.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }