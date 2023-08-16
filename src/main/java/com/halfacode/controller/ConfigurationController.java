package com.halfacode.controller;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.ConfigurationDTO;
import com.halfacode.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostMapping("/save")
    public ApiResponse<String> saveConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        return configurationService.saveConfiguration(configurationDTO);
    }

    @PostMapping("/update")
    public ApiResponse<String> updateConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        return configurationService.updateConfiguration(configurationDTO);
    }
}