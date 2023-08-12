package com.halfacode.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.halfacode.entity.ConfigurationEntity;
import com.halfacode.repoistory.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class AwsS3Configuration {

    private final ConfigurationRepository configurationRepository;

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
            // Return a mock AmazonS3 instance without credentials or handle it according to your needs
            return AmazonS3ClientBuilder.standard().build();
        }
    }
}