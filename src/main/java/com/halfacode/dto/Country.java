package com.halfacode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    private String name;
    private String capital;
    private String region;
    private List<String> cities;

    // Constructors, getters, and setters

    public String getCityByName(String cityName) {
        return cities.stream()
                .filter(city -> city.equalsIgnoreCase(cityName))
                .findFirst()
                .orElse(null); // City not found
    }
}
