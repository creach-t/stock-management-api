package com.inventory.stockmanagementapi.service;

import com.inventory.stockmanagementapi.domain.Category;
import com.inventory.stockmanagementapi.dto.CategoryDTO;
import com.inventory.stockmanagementapi.exception.BusinessException;
import com.inventory.stockmanagementapi.exception.ResourceNotFoundException;
import com.inventory.stockmanagementapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private CategoryDTO testCategoryDTO;

    @BeforeEach
    public void setup() {
        // Setup test data
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");
        testCategory.setProducts(new ArrayList<>());

        testCategoryDTO = new CategoryDTO();
        testCategoryDTO.setId(1L);
        testCategoryDTO.setName("Test Category");
        testCategoryDTO.setDescription("Test Description");
        testCategoryDTO.setProductCount(0);
    }

    @Test
    public void getAllCategories_shouldReturnAllCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<CategoryDTO> result = categoryService.getAllCategories();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testCategory.getId(), result.get(0).getId());
        assertEquals(testCategory.getName(), result.get(0).getName());
        assertEquals(testCategory.getDescription(), result.get(0).getDescription());
        assertEquals(testCategory.getProducts().size(), result.get(0).getProductCount());
    }

    @Test
    public void getCategoryById_withValidId_shouldReturnCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // Act
        CategoryDTO result = categoryService.getCategoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCategory.getId(), result.getId());
        assertEquals(testCategory.getName(), result.getName());
        assertEquals(testCategory.getDescription(), result.getDescription());
    }

    @Test
    public void getCategoryById_withInvalidId_shouldThrowException() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(999L);
        });
    }

    @Test
    public void createCategory_withValidData_shouldReturnCreatedCategory() {
        // Arrange
        CategoryDTO newCategoryDTO = new CategoryDTO();
        newCategoryDTO.setName("New Category");
        newCategoryDTO.setDescription("New Description");

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("New Category");
        newCategory.setDescription("New Description");
        newCategory.setProducts(new ArrayList<>());

        when(categoryRepository.existsByName(newCategoryDTO.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        // Act
        CategoryDTO result = categoryService.createCategory(newCategoryDTO);

        // Assert
        assertNotNull(result);
        assertEquals(newCategory.getId(), result.getId());
        assertEquals(newCategory.getName(), result.getName());
        assertEquals(newCategory.getDescription(), result.getDescription());
        assertEquals(0, result.getProductCount());
    }

    @Test
    public void createCategory_withExistingName_shouldThrowException() {
        // Arrange
        CategoryDTO newCategoryDTO = new CategoryDTO();
        newCategoryDTO.setName("Existing Name");
        newCategoryDTO.setDescription("New Description");

        when(categoryRepository.existsByName(newCategoryDTO.getName())).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            categoryService.createCategory(newCategoryDTO);
        });
    }

    @Test
    public void updateCategory_withValidData_shouldReturnUpdatedCategory() {
        // Arrange
        Long categoryId = 1L;
        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setDescription("Updated Description");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Original Name");
        existingCategory.setDescription("Original Description");
        existingCategory.setProducts(new ArrayList<>());

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updateDTO.getName());
        updatedCategory.setDescription(updateDTO.getDescription());
        updatedCategory.setProducts(new ArrayList<>());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName(updateDTO.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        CategoryDTO result = categoryService.updateCategory(categoryId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals(updateDTO.getName(), result.getName());
        assertEquals(updateDTO.getDescription(), result.getDescription());
    }

    @Test
    public void deleteCategory_withValidId_shouldDeleteCategory() {
        // Arrange
        Long categoryId = 1L;
        Category categoryToDelete = new Category();
        categoryToDelete.setId(categoryId);
        categoryToDelete.setName("Category to Delete");
        categoryToDelete.setProducts(new ArrayList<>());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryToDelete));

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository, times(1)).delete(categoryToDelete);
    }

    @Test
    public void deleteCategory_withCategoryHavingProducts_shouldThrowException() {
        // Arrange
        Long categoryId = 1L;
        Category categoryWithProducts = new Category();
        categoryWithProducts.setId(categoryId);
        categoryWithProducts.setName("Category with Products");
        
        // Add a dummy product to the list
        categoryWithProducts.setProducts(Arrays.asList(new com.inventory.stockmanagementapi.domain.Product()));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryWithProducts));

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });
    }
}
