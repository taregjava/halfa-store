package com.halfacode.config;

import com.halfacode.entity.ConfigurationEntity;
import com.halfacode.repoistory.ConfigurationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;


@Configuration
public class AwsConfigurationInitializer {
  //  private final AwsProperties awsConfiguration;
  private final ConfigurationRepository configurationRepository;

    @Autowired
    public AwsConfigurationInitializer(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @PostConstruct
    public void initializeAwsConfiguration() {
        // Retrieve AWS configuration values from the database
        Optional<ConfigurationEntity> awsAccessKeyIdConfig = configurationRepository.findByKey("aws.accessKeyId");
        Optional<ConfigurationEntity> awsSecretKeyConfig = configurationRepository.findByKey("aws.secretKey");

        if (awsAccessKeyIdConfig.isPresent() && awsSecretKeyConfig.isPresent()) {
            String accessKeyId = awsAccessKeyIdConfig.get().getValue();
            String secretKey = awsSecretKeyConfig.get().getValue();

            // Use the retrieved AWS configuration values
            // For example, you can configure AWS services using these values
        } else {
            // Handle the case when configuration is missing
            // You can log a message or take other actions
            System.out.println("AWS configuration not found in the database. Some features may not work.");
        }
    }
}