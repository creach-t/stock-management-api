package com.inventory.stockmanagementapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.stockmanagementapi.dto.CategoryDTO;
import com.inventory.stockmanagementapi.exception.BusinessException;
import com.inventory.stockmanagementapi.exception.ResourceNotFoundException;
import com.inventory.stockmanagementapi.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO testCategoryDTO;

    @BeforeEach
    public void setup() {
        testCategoryDTO = new CategoryDTO();
        testCategoryDTO.setId(1L);
        testCategoryDTO.setName("Test Category");
        testCategoryDTO.setDescription("Test Description");
        testCategoryDTO.setProductCount(0);
    }

    @Test
    public void getAllCategories_shouldReturnCategories() throws Exception {
        // Arrange
        List<CategoryDTO> categories = Arrays.asList(testCategoryDTO);
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testCategoryDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(testCategoryDTO.getName())))
                .andExpect(jsonPath("$[0].description", is(testCategoryDTO.getDescription())))
                .andExpect(jsonPath("$[0].productCount", is(testCategoryDTO.getProductCount())));
    }

    @Test
    public void getCategoryById_withValidId_shouldReturnCategory() throws Exception {
        // Arrange
        when(categoryService.getCategoryById(1L)).thenReturn(testCategoryDTO);

        // Act & Assert
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCategoryDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testCategoryDTO.getName())))
                .andExpect(jsonPath("$.description", is(testCategoryDTO.getDescription())))
                .andExpect(jsonPath("$.productCount", is(testCategoryDTO.getProductCount())));
    }

    @Test
    public void getCategoryById_withInvalidId_shouldReturn404() throws Exception {
        // Arrange
        when(categoryService.getCategoryById(999L)).thenThrow(new ResourceNotFoundException("Category", "id", 999L));

        // Act & Assert
        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createCategory_withValidData_shouldReturnCreatedCategory() throws Exception {
        // Arrange
        CategoryDTO inputDTO = new CategoryDTO();
        inputDTO.setName("New Category");
        inputDTO.setDescription("New Description");

        CategoryDTO createdDTO = new CategoryDTO();
        createdDTO.setId(2L);
        createdDTO.setName("New Category");
        createdDTO.setDescription("New Description");
        createdDTO.setProductCount(0);

        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(createdDTO);

        // Act & Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(createdDTO.getName())))
                .andExpect(jsonPath("$.description", is(createdDTO.getDescription())))
                .andExpect(jsonPath("$.productCount", is(createdDTO.getProductCount())));
    }

    @Test
    public void createCategory_withDuplicateName_shouldReturn400() throws Exception {
        // Arrange
        CategoryDTO inputDTO = new CategoryDTO();
        inputDTO.setName("Duplicate Name");
        inputDTO.setDescription("Some Description");

        when(categoryService.createCategory(any(CategoryDTO.class)))
                .thenThrow(new BusinessException("A category with the name 'Duplicate Name' already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCategory_withValidData_shouldReturnUpdatedCategory() throws Exception {
        // Arrange
        Long categoryId = 1L;
        CategoryDTO inputDTO = new CategoryDTO();
        inputDTO.setName("Updated Name");
        inputDTO.setDescription("Updated Description");

        CategoryDTO updatedDTO = new CategoryDTO();
        updatedDTO.setId(categoryId);
        updatedDTO.setName("Updated Name");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setProductCount(0);

        when(categoryService.updateCategory(eq(categoryId), any(CategoryDTO.class))).thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedDTO.getName())))
                .andExpect(jsonPath("$.description", is(updatedDTO.getDescription())))
                .andExpect(jsonPath("$.productCount", is(updatedDTO.getProductCount())));
    }

    @Test
    public void deleteCategory_withValidId_shouldReturn204() throws Exception {
        // Arrange
        Long categoryId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/categories/" + categoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCategory_withNonEmptyCategory_shouldReturn400() throws Exception {
        // Arrange
        Long categoryId = 1L;
        doThrow(new BusinessException("Cannot delete category with existing products. Remove or reassign products first."))
                .when(categoryService).deleteCategory(categoryId);

        // Act & Assert
        mockMvc.perform(delete("/api/categories/" + categoryId))
                .andExpect(status().isBadRequest());
    }
}
