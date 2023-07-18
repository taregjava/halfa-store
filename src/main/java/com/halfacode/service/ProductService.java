package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Product;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.mapper.ProductMapper;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.specifiaction.ProductSpecifications;
import com.halfacode.util.HalfaStoreUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final S3Service s3Service;
    @Autowired
    public ProductService(ProductMapper productMapper, ProductRepository productRepository, ImageService imageService, CategoryService categoryService, S3Service s3Service) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.s3Service = s3Service;
    }

    public ApiResponse<ProductDTO> getProductById(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            ProductDTO productDTO = productMapper.buildProductDTO(product);

            return new ApiResponse<>(HttpStatus.OK.value(), productDTO, null, LocalDateTime.now());
        } catch (ProductNotFoundException ex) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, ex.getMessage(), LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving the product", LocalDateTime.now());
        }
    }

    public ApiResponse<List<ProductDTO>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            List<ProductDTO> productDTOs = new ArrayList<>();

            for (Product product : products) {
                ProductDTO productDTO = productMapper.mapEntityToDto(product);

                // Retrieve image data for the product from S3
                String imageData = s3Service.getImageUrl(product.getName());
                productDTO.setImageName(imageData);

                productDTOs.add(productDTO);
            }

            return new ApiResponse<>(HttpStatus.OK.value(), productDTOs, null, LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving the products", LocalDateTime.now());
        }
    }
    public ApiResponse<ProductDTO> createProduct(ProductDTO productDTO, MultipartFile imageFile) {
        try {
            // Upload the image file to S3
            ApiResponse<String> imageResponse = s3Service.uploadFile("images", productDTO.getName(), imageFile.getInputStream());
            if (imageResponse.getStatus() != HttpStatus.OK.value()) {
                return new ApiResponse<>(imageResponse.getStatus(), null, imageResponse.getError(), LocalDateTime.now());
            }
            String imageName = imageResponse.getPayload();

            ApiResponse<CategoryDTO> categoryResponse = categoryService.getCategoryById(productDTO.getCategoryId());
            if (categoryResponse.getStatus() != HttpStatus.OK.value()) {
                return new ApiResponse<>(categoryResponse.getStatus(), null, categoryResponse.getError(), LocalDateTime.now());
            }
            CategoryDTO categoryDTO = categoryResponse.getPayload();
            Product product = productMapper.mapDtoToEntity(productDTO);
            float discountPercent = HalfaStoreUtility.calculateDiscountPercent(productDTO.getCost(), productDTO.getPrice());
            product.setDiscountPercent(discountPercent);
            Product createdProduct = productRepository.save(product);
            ProductDTO createdProductDTO = productMapper.mapEntityToDto(createdProduct);
            createdProductDTO.setImageName(imageName);
            return new ApiResponse<>(HttpStatus.OK.value(), createdProductDTO, LocalDateTime.now());
        } catch (IOException ex) {
            LOGGER.error("An error occurred while reading the image file", ex);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while reading the image file", LocalDateTime.now());
        } catch (Exception ex) {
            LOGGER.error("An error occurred while creating the product", ex);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while creating the product", LocalDateTime.now());
        }
    }

    public ApiResponse<ProductDTO> updateProduct(Long productId, ProductDTO productDTO, MultipartFile imageFile) {
        try {
            // Retrieve the existing product from the database
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isEmpty()) {
                return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, "Product not found", LocalDateTime.now());
            }
            Product existingProduct = optionalProduct.get();

            // Update product fields from the DTO using the mapper
            productMapper.updateEntity(existingProduct, productDTO);

            // If an image file is provided, upload it to S3 and update the image name
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = s3Service.uploadFile("images", productDTO.getName(), imageFile.getInputStream()).getPayload();
                existingProduct.setImageName(imageName);
            }

            // Save the updated product entity
            Product updatedProduct = productRepository.save(existingProduct);
            ProductDTO updatedProductDTO = productMapper.mapEntityToDto(updatedProduct);

            return new ApiResponse<>(HttpStatus.OK.value(), updatedProductDTO, null, LocalDateTime.now());
        } catch (IOException ex) {
            LOGGER.error("An error occurred while reading the image file", ex);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while reading the image file", LocalDateTime.now());
        } catch (Exception ex) {
            LOGGER.error("An error occurred while updating the product", ex);
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while updating the product", LocalDateTime.now());
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDTO> searchProducts(ProductDTO searchCriteria) {
        Specification<Product> spec = Specification.where(null);

        if (searchCriteria.getName() != null) {
            spec = spec.and(ProductSpecifications.hasName(searchCriteria.getName()));
        }
        // Add other specifications based on your criteria

        List<Product> products = productRepository.findAll(spec);
        return productMapper.mapEntityListToDtoList(products);
    }


}