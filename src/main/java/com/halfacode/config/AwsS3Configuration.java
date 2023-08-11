package com.halfacode.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.halfacode.entity.ConfigurationEntity;
import com.halfacode.repoistory.ConfigurationRepository;
import jakarta.persistence.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class AwsS3Configuration {

    private final ConfigurationRepository configurationRepository;
    private AmazonS3 cachedAmazonS3;

    @Autowired
    public AwsS3Configuration(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Bean
    public AmazonS3 amazonS3() {
        Optional<ConfigurationEntity> awsAccessKeyIdConfig = configurationRepository.findByKey("aws.accessKeyId");
        Optional<ConfigurationEntity> awsSecretKeyConfig = configurationRepository.findByKey("aws.secretKey");

        if (awsAccessKeyIdConfig.isPresent() && awsSecretKeyConfig.isPresent()) {
            String accessKeyId = awsAccessKeyIdConfig.get().getValue();
            String secretKey = awsSecretKeyConfig.get().getValue();

            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);

            return AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();
        } else {
            // Handle the case when AWS configuration is missing
            // You can log a message or take other appropriate actions
            System.out.println("AWS accessKeyId and/or secretKey not found in the database. AmazonS3 bean not created.");
            return null; // Or provide default values if needed
        }
    }

}