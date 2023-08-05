package com.halfacode.config;

import com.halfacode.mapper.CategoryMapper;
import com.halfacode.mapper.ProductMapper;
import com.halfacode.mapper.UserMapper;
import com.halfacode.repoistory.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
public class TestConfig {
    @Bean
    public CategoryMapper categoryMapper() {
        return Mockito.mock(CategoryMapper.class);
    }

    @Bean
    public S3Client s3Client() {
        // Create a mock S3Client using Mockito
        return Mockito.mock(S3Client.class);
    }

    @Bean
    @Primary // Add the @Primary annotation to specify this bean as the primary bean for CartServiceTests
    public OrderRepository orderRepository() {
        // Create a mock OrderRepository using Mockito
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    @Primary // Add the @Primary annotation to specify this bean as the primary bean for CartServiceTests
    public OrderDetailRepository orderDetailRepository() {
        // Create a mock OrderDetailRepository using Mockito
        return Mockito.mock(OrderDetailRepository.class);
    }
    @Bean // Create a mock OrderTrackRepository and include it as a bean
    public OrderTrackRepository orderTrackRepository() {
        return Mockito.mock(OrderTrackRepository.class);
    }

    @Bean // Create a mock RoleRepository and include it as a bean
    public RoleRepository roleRepository() {
        return Mockito.mock(RoleRepository.class);
    }

    @Bean // Create a mock UserMapper and include it as a bean
    public UserMapper userMapper() {
        return Mockito.mock(UserMapper.class);
    }

    @Bean // Create a mock UserMapper and include it as a bean
    public ProductMapper productMapper() {
        return Mockito.mock(ProductMapper.class);
    }

    @Bean // Create a mock ShipmentRepository and include it as a bean
    public ShipmentRepository shipmentRepository() {
        return Mockito.mock(ShipmentRepository.class);
    }
}