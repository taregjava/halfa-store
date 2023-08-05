package com.halfacode.service;

import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Product;
import com.halfacode.mapper.ProductMapper;
import com.halfacode.repoistory.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testSearchProducts() {
        // Create sample search criteria (you can customize this based on your test case)
        ProductDTO searchCriteria = new ProductDTO();
        searchCriteria.setName("Sample Product");

        // Create a sample product entity and product DTO
        Product sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Sample Product");
        // ... Set other properties as needed

        ProductDTO sampleProductDTO = new ProductDTO();
        sampleProductDTO.setId(1L);
        sampleProductDTO.setName("Sample Product");
        // ... Set other properties as needed

        // Mock the repository method to return a list with the sample product
        when(productRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(sampleProduct));

        // Mock the mapper to return the product DTO when mapping the sample product entity
        when(productMapper.mapEntityToDto(sampleProduct)).thenReturn(sampleProductDTO);

        // Call the searchProducts method with the sample search criteria
        List<ProductDTO> result = productService.searchProducts(searchCriteria);

        // Assertions
        assertFalse(result.isEmpty());
        assertEquals(sampleProductDTO, result.get(0));
    }
}