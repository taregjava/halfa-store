package com.halfacode.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class AwsProperties {

    //@Value("${aws.accessKeyId}")
    private String accessKeyId;

    //@Value("${aws.secretKey}")
    private String secretKey;

    // Getters for accessKeyId and secretKey

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretKey() {
        return secretKey;
    }
}