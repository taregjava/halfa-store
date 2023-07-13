package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.mapper.ProductMapper;
import com.halfacode.repoistory.CategoryRepository;
import com.halfacode.repoistory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductMapper productMapper, ProductRepository productRepository, ImageService imageService, CategoryService categoryService) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.imageService = imageService;
        this.categoryService = categoryService;
    }

    public ApiResponse<ProductDTO> getProductById(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            ProductDTO productDTO = productMapper.mapEntityToDto(product);

            return new ApiResponse<>(HttpStatus.OK.value(), productDTO, null, LocalDateTime.now());
        } catch (ProductNotFoundException ex) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, ex.getMessage(), LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving the product", LocalDateTime.now());
        }
    }
    // Existing methods...

   /* public List<Product> getProductsByCategory(Long categoryId) {
        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        return productRepository.findByCategory(category);
    }*/

    public ApiResponse<List<ProductDTO>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            List<ProductDTO> productDTOs = productMapper.mapEntityListToDtoList(products);

            return new ApiResponse<>(HttpStatus.OK.value(), productDTOs, null, LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving the products", LocalDateTime.now());
        }
    }
    public ApiResponse<ProductDTO> createProduct(ProductDTO productDTO, MultipartFile imageFile) {
        try {
            ApiResponse<String> imageResponse = imageService.saveFile(productDTO.getName(), imageFile);
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
            Product createdProduct = productRepository.save(product);
            ProductDTO createdProductDTO = productMapper.mapEntityToDto(createdProduct);

            return new ApiResponse<>(HttpStatus.OK.value(), createdProductDTO, "Product created successfully", LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while creating the product", LocalDateTime.now());
        }
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}