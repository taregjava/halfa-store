package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.dto.ProductDTO;
import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import com.halfacode.exception.ProductNotFoundException;
import com.halfacode.mapper.ProductMapper;
import com.halfacode.repoistory.ProductRepository;
import com.halfacode.specifiaction.ProductSpecifications;
import com.halfacode.specifiaction.WrappedSpecification;
import com.halfacode.util.HalfaStoreUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            List<ProductDTO> productDTOs = products.stream()
                    .map(product -> productMapper.buildProductDTO(product))
                    .peek(productDTO -> {
                        String imageData = s3Service.getImageUrl(productDTO.getName());
                        productDTO.setImageName(imageData);
                    })
                    .collect(Collectors.toList());

            return new ApiResponse<>(HttpStatus.OK.value(), productDTOs, null, LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving the products", LocalDateTime.now());
        }
    }
    public ApiResponse<ProductDTO> createProduct(ProductDTO productDTO, MultipartFile imageFile) {
        try {
            // Upload the image file to S3
            ApiResponse<String> imageResponse = uploadImageToS3(productDTO, imageFile);
            if (!imageResponse.isSuccessful()) {
                return new ApiResponse<>(imageResponse.getStatus(), null, imageResponse.getError(), LocalDateTime.now());
            }
            String imageName = imageResponse.getPayload();

            ApiResponse<CategoryDTO> categoryResponse = getCategoryById(productDTO.getCategoryId());
            if (!categoryResponse.isSuccessful()) {
                String errorMessage = "Invalid category ID: " + (productDTO.getCategoryId() != null ? productDTO.getCategoryId() : "null");
                return new ApiResponse<>(categoryResponse.getStatus(), null, errorMessage, LocalDateTime.now());
            }
            CategoryDTO categoryDTO = categoryResponse.getPayload();
            Product createdProduct = saveProduct(productDTO, imageName);
            ProductDTO createdProductDTO = productMapper.mapEntityToDto(createdProduct);
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
        // Build the specification based on the search criteria
        Specification<Product> spec = ProductSpecifications.buildSpecification(searchCriteria);

        // You can add more specifications here based on other search criteria
        // For example:
        // if (searchCriteria.getEnabled() != null) {
        //     spec = spec.and(ProductSpecifications.isEnabled(searchCriteria.getEnabled()));
        // }

        // Execute the query with the built specification
        List<Product> products = productRepository.findAll(spec);

        // Check if any products are found and throw an exception if none are found
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found for the given search criteria.");
        }

        // Map the found products to DTOs and return the list
        return productMapper.mapEntityListToDtoList(products);
    }




    public ApiResponse<List<ProductDTO>> getProductsByCategory(Long categoryId) {
        try {
            ApiResponse<CategoryDTO> categoryResponse = categoryService.getCategoryById(categoryId);
            if (categoryResponse.getStatus() != HttpStatus.OK.value()) {
                return new ApiResponse<>(categoryResponse.getStatus(), null, categoryResponse.getError(), LocalDateTime.now());
            }

            CategoryDTO categoryDTO = categoryResponse.getPayload();
            Category category = ProductMapper.convertToCategory(categoryDTO);

            List<Product> products = productRepository.findByCategory(category);
            List<ProductDTO> productDTOs = productMapper.mapEntityListToDtoList(products);

            return new ApiResponse<>(HttpStatus.OK.value(), productDTOs, null, LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving products by category", LocalDateTime.now());
        }
    }
    public ApiResponse<List<ProductDTO>> getProductsByAllCtegory() {
        try {

            List<Product> products = productRepository.findAll();
            List<ProductDTO> productDTOs = productMapper.mapEntityListToDtoList(products);

            return new ApiResponse<>(HttpStatus.OK.value(), productDTOs, null, LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving products by category", LocalDateTime.now());
        }
    }

    public ApiResponse<List<ProductDTO>> getBestSellingProducts() {
        try {
            List<Product> bestSellingProducts = calculateBestSellingProducts(); // Implement this method
            List<ProductDTO> bestSellingProductDTOs = productMapper.mapEntityListToDtoList(bestSellingProducts);

            return new ApiResponse<>(HttpStatus.OK.value(), bestSellingProductDTOs, null, LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving best-selling products", LocalDateTime.now());
        }
    }
    public ApiResponse<List<ProductDTO>> getDealOfTheDayProducts() {
        try {
            List<Product> dealOfTheDayProducts = calculateDealOfTheDayProducts(); // Implement this method
            List<ProductDTO> dealOfTheDayProductDTOs = productMapper.mapEntityListToDtoList(dealOfTheDayProducts);

            return new ApiResponse<>(HttpStatus.OK.value(), dealOfTheDayProductDTOs, null, LocalDateTime.now());
        } catch (Exception ex) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "An error occurred while retrieving Deal of the Day products", LocalDateTime.now());
        }
    }

    private List<Product> calculateDealOfTheDayProducts() {
        // Define your logic for selecting the deal of the day products here
        // For example, you could select products that have a special "deal of the day" tag,
        // or products that have a high discount percentage, etc.

        // For demonstration purposes, let's assume you want to select products with a discount
        // percentage greater than a certain threshold (e.g., 20%).

        float discountThreshold = 20.0f; // Example threshold

        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasDiscountPercentGreaterThan(discountThreshold));

        return productRepository.findAll(spec);
    }

    public List<Product> calculateBestSellingProducts() {
        // Implement your logic to calculate best-selling products here
        // This could involve analyzing sales data, order history, reviews, etc.

        // For demonstration purposes, let's assume you have a list of products
        // and you want to sort them based on their discount percentage in descending order
        List<Product> allProducts = productRepository.findAll();

        // Sort products based on discount percentage (higher discounts first)
        allProducts.sort((a, b) -> Float.compare(b.getDiscountPercent(), a.getDiscountPercent()));

        // Return the top N best-selling products, where N is the number you want to show
        int numberOfBestSellingProducts = 10; // Change this as needed
        return allProducts.subList(0, Math.min(numberOfBestSellingProducts, allProducts.size()));
    }

    private ApiResponse<String> uploadImageToS3(ProductDTO productDTO, MultipartFile imageFile) throws IOException {
        return s3Service.uploadFile("images", productDTO.getName(), imageFile.getInputStream());
    }

    private ApiResponse<CategoryDTO> getCategoryById(Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    private Product saveProduct(ProductDTO productDTO, String imageName) {
        Product product = productMapper.mapDtoToEntity(productDTO);
        float discountPercent = HalfaStoreUtility.calculateDiscountPercent(productDTO.getCost(), productDTO.getPrice());
        product.setDiscountPercent(discountPercent);
        product.setImageName(imageName);
        return productRepository.save(product);
    }

}