package com.halfacode.service;

import com.halfacode.dto.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CountryService {

    private final RestTemplate restTemplate;

    public CountryService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Country> getAllCountries() {
        String url = "https://restcountries.com/v2/all";

        ResponseEntity<Country[]> response = restTemplate.getForEntity(url, Country[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Arrays.asList(response.getBody());
        }

        return Collections.emptyList();
    }
    public Country getCountryByName(String countryName) {
        List<Country> countries = getAllCountries();
        return countries.stream()
                .filter(country -> country.getName().equalsIgnoreCase(countryName))
                .findFirst()
                .orElse(null); // Country not found
    }
    public String getCityByName(String cityName, String countryName) {
        List<Country> countries = getAllCountries();
        return countries.stream()
                .filter(country -> country.getName().equalsIgnoreCase(countryName))
                .flatMap(country -> country.getCities().stream())
                .filter(city -> city.equalsIgnoreCase(cityName))
                .findFirst()
                .orElse(null); // City not found
    }

    /*public String getCityByName(String cityName, String countryName) {
    return countries.stream()
            .filter(country -> country.getName().equalsIgnoreCase(countryName))
            .flatMap(country -> country.getCities().stream())
            .filter(city -> city.equalsIgnoreCase(cityName))
            .findFirst()
            .orElse(null); // City not found
}*/
}