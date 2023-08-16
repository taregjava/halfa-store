package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.dto.CategoryDTO;
import com.halfacode.entity.Category;
import com.halfacode.exception.NotFoundException;
import com.halfacode.mapper.CategoryMapper;
import com.halfacode.repoistory.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

//@SpringBootTest
//@ActiveProfiles("test")
//@DisplayName("CategoryService Tests")
public class CategoryServiceTests {

  /*  @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private S3Service imageService;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCategory_Successful() throws Exception {
        // Prepare test data
        String categoryName = "Test Category";
        MultipartFile imageFile = new MockMultipartFile("test-image", "test-image.jpg", "image/jpeg", new byte[0]);

        // Mock S3Service response
        ApiResponse<String> imageResponse = new ApiResponse<>(HttpStatus.OK.value(), "test-image.jpg", null, null);
        when(imageService.uploadFile(eq("categories"), eq(categoryName), any(InputStream.class))).thenReturn(imageResponse);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(categoryName);

        Category category = new Category();
        category.setName(categoryName); // Set the name using the setter method
        category.setImageName("test-image.jpg");

        when(categoryMapper.mapDtoToEntity(any(CategoryDTO.class))).thenReturn(category);

        Category createdCategory = new Category();
        createdCategory.setId(1L);
        createdCategory.setName(categoryName); // Set the name using the setter method
        createdCategory.setImageName("test-image.jpg");
        when(categoryRepository.save(any(Category.class))).thenReturn(createdCategory);

        // Perform the createCategory method
        ResponseEntity<ApiResponse<CategoryDTO>> responseEntity = categoryService.createCategory(categoryName, imageFile);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getPayload());
        assertEquals(categoryName, responseEntity.getBody().getPayload().getName());
        assertEquals("test-image.jpg", responseEntity.getBody().getPayload().getImageName());
    }

    @Test
    public void testCreateCategory_ImageUploadError() throws Exception {
        // Prepare test data
        String categoryName = "Test Category";
        MultipartFile imageFile = new MockMultipartFile("test-image", "test-image.jpg", "image/jpeg", new byte[0]);

        // Mock S3Service response with an error
        ApiResponse<String> imageResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Image upload failed", null);
        when(imageService.uploadFile(eq("categories"), eq(categoryName), any(InputStream.class))).thenReturn(imageResponse);

        // Perform the createCategory method
        ResponseEntity<ApiResponse<CategoryDTO>> responseEntity = categoryService.createCategory(categoryName, imageFile);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getBody().getStatus());
        assertNull(responseEntity.getBody().getPayload());
        assertEquals("Image upload failed", responseEntity.getBody().getError());
    }
    @Test
    public void testGetAllCategories_Successful() {
        // Prepare test data
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setImageName("image1.jpg");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setImageName("image2.jpg");

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1L);
        categoryDTO1.setName("Category 1");
        categoryDTO1.setImageName("image1.jpg");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(2L);
        categoryDTO2.setName("Category 2");
        categoryDTO2.setImageName("image2.jpg");

        when(categoryMapper.mapEntityToDto(category1)).thenReturn(categoryDTO1);
        when(categoryMapper.mapEntityToDto(category2)).thenReturn(categoryDTO2);

        // Perform the getAllCategories method
        ApiResponse<List<CategoryDTO>> response = categoryService.getAllCategories();

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(2, response.getPayload().size());
    }

    @Test
    public void testGetCategoryById_Successful() {
        // Prepare test data
        Long categoryId = 1L;
        String categoryName = "Category 1";
        String imageName = "image1.jpg";

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        category.setImageName(imageName);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);
        categoryDTO.setName(categoryName);
        categoryDTO.setImageName(imageName);

        when(categoryMapper.mapEntityToDto(category)).thenReturn(categoryDTO);

        // Perform the getCategoryById method
        ApiResponse<CategoryDTO> response = categoryService.getCategoryById(categoryId);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getPayload());
        assertEquals(categoryName, response.getPayload().getName());
        assertEquals(imageName, response.getPayload().getImageName());
    }

    @Test
    public void testGetCategoryById_NotFound() {
        // Prepare test data
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Perform the getCategoryById method
        ApiResponse<CategoryDTO> response = categoryService.getCategoryById(categoryId);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertNull(response.getPayload());
        assertNotNull(response.getError());
    }*/
}