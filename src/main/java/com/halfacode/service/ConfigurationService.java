package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.ConfigurationDTO;
import com.halfacode.entity.ConfigurationEntity;
import com.halfacode.repoistory.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public ApiResponse<String> saveConfiguration(ConfigurationDTO configurationDTO) {
        ConfigurationEntity configuration = new ConfigurationEntity();
        configuration.setKey(configurationDTO.getKey());
        configuration.setValue(configurationDTO.getValue());

        configurationRepository.save(configuration);

        return new ApiResponse<>(HttpStatus.OK.value(), "Configuration saved successfully", LocalDateTime.now());
    }

    public ApiResponse<String> updateConfiguration(ConfigurationDTO configurationDTO) {
        Optional<ConfigurationEntity> existingConfiguration = configurationRepository.findByKey(configurationDTO.getKey());
        if (existingConfiguration.isPresent()) {
            ConfigurationEntity configuration = existingConfiguration.get();
            configuration.setValue(configurationDTO.getValue());

            configurationRepository.save(configuration);

            return new ApiResponse<>(HttpStatus.OK.value(), "Configuration updated successfully", LocalDateTime.now());
        } else {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Configuration not found", LocalDateTime.now());
        }
    }
}