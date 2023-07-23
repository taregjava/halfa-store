package com.halfacode.service;

import com.halfacode.dto.Country;
import com.halfacode.dto.DeliveryOption;
import com.halfacode.dto.LocationInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryService {

    private final CountryService countryService;

    public DeliveryService(CountryService countryService) {
        this.countryService = countryService;
    }
    public List<DeliveryOption> getRecommendedOptions(LocationInfo locationInfo) {
        List<DeliveryOption> recommendedOptions = new ArrayList<>();

        // Fetch the country based on the locationInfo
        String countryName = locationInfo.getCountry();
        Country country = countryService.getCountryByName(countryName);

        // Apply logic based on the country information
        if (country != null) {
            // Add delivery options based on the country's properties
            if (country.getRegion().equalsIgnoreCase("Europe")) {
                recommendedOptions.add(new DeliveryOption("Option 1", "Fast delivery within Europe", 10.0));
                recommendedOptions.add(new DeliveryOption("Option 2", "Standard delivery within Europe", 5.0));
            } else {
                recommendedOptions.add(new DeliveryOption("Option 3", "International delivery", 20.0));
            }
        }

        return recommendedOptions;
    }
}