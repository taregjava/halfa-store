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
        response.setMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(IllegalArgumentException ex) {
        // Create a custom ApiResponse with the validation error message and HTTP status code
        ApiResponse<Object> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getErrorMessage());
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
            apiResponse.setMessage("Validation error");
            apiResponse.setTimestamp(LocalDateTime.now());
            apiResponse.setPayload(null);
          //  apiResponse.setErrors(errors);

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
            apiResponse.setMessage("Validation error");
            apiResponse.setTimestamp(LocalDateTime.now());
            apiResponse.setPayload(null);
           // apiResponse.setErrors(errors);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex) {
            ApiResponse<Void> apiResponse = new ApiResponse<>();
            apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Validation error");
            apiResponse.setTimestamp(LocalDateTime.now());
            apiResponse.setPayload(null);
           // apiResponse.getErrors().add(ex.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

   /* @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "Validation error: " + ex.getBindingResult().getFieldError().getDefaultMessage();

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        apiResponse.setError(errorMessage);
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setPayload(null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }*/

    @ExceptionHandler(RoleCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleRoleCreationException(RoleCreationException ex) {
        ApiResponse<Void> response = new ApiResponse<>(400, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    }