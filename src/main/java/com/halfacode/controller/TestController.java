package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping("/basic")
    public String testBasic(){
        return "basic authentication";
    }
    @GetMapping
   // @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> test(HttpServletRequest request) {
        ApiResponse<Boolean> apiKeyResponse = isValidApiKey(request);

        if (apiKeyResponse.getPayload()) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }
    private ApiResponse<Boolean> isValidApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-Key");
        boolean isValid = apiKey != null && apiKey.equals("your_valid_api_key_here");

        // Constructing the ApiResponse instance
        int statusCode = isValid ? 200 : 401; // Valid or Unauthorized status
        String message = isValid ? "API key is valid" : "Unauthorized";
        ApiResponse<Boolean> apiResponse = new ApiResponse<>(isValid, message);

        return apiResponse;
    }
}
