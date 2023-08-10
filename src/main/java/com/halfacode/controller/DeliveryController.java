package com.halfacode.controller;

import com.halfacode.dto.*;
import com.halfacode.exception.NotFoundException;
import com.halfacode.service.CountryService;
import com.halfacode.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final CountryService countryService;

    public DeliveryController(DeliveryService deliveryService, CountryService countryService) {
        this.deliveryService = deliveryService;
        this.countryService = countryService;
    }

    @GetMapping("/countries")
    public List<Country> getCountries() {
        return countryService.getAllCountries();
    }

    @PostMapping("/recommend")
    public ResponseEntity<ApiResponse<List<DeliveryOption>>> recommendDeliveryOptions(@RequestBody LocationInfo locationInfo) {
        try {
            List<DeliveryOption> recommendedOptions = deliveryService.getRecommendedOptions(locationInfo);

            ApiResponse<List<DeliveryOption>> response = new ApiResponse<>();
            response.setStatus(HttpStatus.OK.value());
            response.setPayload(recommendedOptions);
            response.setTimestamp(LocalDateTime.now());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<DeliveryOption>> response = new ApiResponse<>();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/byName/{countryName}")
    public ResponseEntity<ApiResponse<Country>> getCountryByName(@PathVariable String countryName) {
        try {
            Country country = countryService.getCountryByName(countryName);
            if (country == null) {
                throw new NotFoundException("Country not found with name: " + countryName);
            }

            ApiResponse<Country> response = new ApiResponse<>();
            response.setStatus(HttpStatus.OK.value());
            response.setPayload(country);
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Country> response = new ApiResponse<>();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<String>> getCityByName(
            @RequestParam("cityName") String cityName,
            @RequestParam("countryName") String countryName
    ) {
        try {
            String city = countryService.getCityByName(cityName, countryName);
            if (city == null) {
                throw new NotFoundException("City not found with name: " + cityName);
            }

            ApiResponse<String> response = new ApiResponse<>();
            response.setStatus(HttpStatus.OK.value());
            response.setPayload(city);
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}