package com.inventory.stockmanagementapi.service;

import com.inventory.stockmanagementapi.dto.CategoryDTO;
import com.inventory.stockmanagementapi.exception.BusinessException;
import com.inventory.stockmanagementapi.exception.ResourceNotFoundException;
import com.inventory.stockmanagementapi.domain.Category;
import com.inventory.stockmanagementapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all categories with product count
     * @return List of all categories with their product count
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a category by ID
     * @param id The category ID
     * @return The category DTO
     * @throws ResourceNotFoundException if the category is not found
     */
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return convertToDTO(category);
    }

    /**
     * Create a new category
     * @param categoryDTO The category data to create
     * @return The created category
     * @throws BusinessException if a category with the same name already exists
     */
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BusinessException("A category with the name '" + categoryDTO.getName() + "' already exists");
        }
        
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    /**
     * Update an existing category
     * @param id The category ID to update
     * @param categoryDTO The updated category data
     * @return The updated category
     * @throws ResourceNotFoundException if the category is not found
     * @throws BusinessException if a different category with the same name already exists
     */
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        // Check if name is being changed and if new name already exists
        if (!category.getName().equals(categoryDTO.getName()) && 
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BusinessException("A category with the name '" + categoryDTO.getName() + "' already exists");
        }
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        
        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    /**
     * Delete a category by ID
     * @param id The category ID to delete
     * @throws ResourceNotFoundException if the category is not found
     * @throws BusinessException if the category contains products
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        if (!category.getProducts().isEmpty()) {
            throw new BusinessException("Cannot delete category with existing products. Remove or reassign products first.");
        }
        
        categoryRepository.delete(category);
    }

    /**
     * Convert Category entity to CategoryDTO
     * @param category The Category entity
     * @return The CategoryDTO
     */
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setProductCount(category.getProducts().size());
        return dto;
    }
}
