package com.inventory.stockmanagementapi.service;

import com.inventory.stockmanagementapi.dto.ProductDTO;
import com.inventory.stockmanagementapi.dto.StockUpdateDTO;
import com.inventory.stockmanagementapi.exception.BusinessException;
import com.inventory.stockmanagementapi.exception.ResourceNotFoundException;
import com.inventory.stockmanagementapi.domain.Category;
import com.inventory.stockmanagementapi.domain.Product;
import com.inventory.stockmanagementapi.repository.CategoryRepository;
import com.inventory.stockmanagementapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all products
     * @return List of all products
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get products with pagination
     * @param pageable Pagination information
     * @return A page of products
     */
    public Page<ProductDTO> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Get a product by ID
     * @param id The product ID
     * @return The product DTO
     * @throws ResourceNotFoundException if the product is not found
     */
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return convertToDTO(product);
    }

    /**
     * Create a new product
     * @param productDTO The product data to create
     * @return The created product
     * @throws ResourceNotFoundException if the associated category is not found
     * @throws BusinessException if a product with the same SKU already exists
     */
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Check if SKU already exists
        if (productDTO.getSku() != null && !productDTO.getSku().isEmpty() && 
                productRepository.existsBySku(productDTO.getSku())) {
            throw new BusinessException("A product with the SKU '" + productDTO.getSku() + "' already exists");
        }
        
        // Find the category
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDTO.getCategoryId()));
        
        Product product = new Product();
        updateProductFromDTO(product, productDTO, category);
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    /**
     * Update an existing product
     * @param id The product ID to update
     * @param productDTO The updated product data
     * @return The updated product
     * @throws ResourceNotFoundException if the product or associated category is not found
     * @throws BusinessException if a different product with the same SKU already exists
     */
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        // Check if SKU is being changed and if new SKU already exists
        if (productDTO.getSku() != null && !productDTO.getSku().isEmpty() && 
                !productDTO.getSku().equals(product.getSku()) && 
                productRepository.existsBySku(productDTO.getSku())) {
            throw new BusinessException("A product with the SKU '" + productDTO.getSku() + "' already exists");
        }
        
        // Find the category
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDTO.getCategoryId()));
        
        updateProductFromDTO(product, productDTO, category);
        
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * Delete a product by ID
     * @param id The product ID to delete
     * @throws ResourceNotFoundException if the product is not found
     */
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Update product stock
     * @param stockUpdateDTO The stock update information
     * @return The updated product
     * @throws ResourceNotFoundException if the product is not found
     * @throws BusinessException if the stock operation would result in negative quantity
     */
    @Transactional
    public ProductDTO updateStock(StockUpdateDTO stockUpdateDTO) {
        Product product = productRepository.findById(stockUpdateDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", stockUpdateDTO.getProductId()));
        
        Integer newQuantity;
        
        switch (stockUpdateDTO.getOperationType()) {
            case ADD:
                newQuantity = product.getQuantity() + stockUpdateDTO.getQuantityChange();
                break;
            case REMOVE:
                newQuantity = product.getQuantity() - stockUpdateDTO.getQuantityChange();
                if (newQuantity < 0) {
                    throw new BusinessException("Cannot remove more items than available in stock. Current stock: " + product.getQuantity());
                }
                break;
            case SET:
                if (stockUpdateDTO.getQuantityChange() < 0) {
                    throw new BusinessException("Cannot set stock to a negative value");
                }
                newQuantity = stockUpdateDTO.getQuantityChange();
                break;
            default:
                throw new BusinessException("Invalid operation type");
        }
        
        product.setQuantity(newQuantity);
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * Get products with low stock (below threshold)
     * @param threshold The threshold quantity
     * @return List of products with stock below threshold
     */
    public List<ProductDTO> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search products by name or description
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return A page of matching products
     */
    public Page<ProductDTO> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.searchProducts(searchTerm, pageable)
                .map(this::convertToDTO);
    }

    /**
     * Get products by category
     * @param categoryId The category ID
     * @param pageable Pagination information
     * @return A page of products in the category
     */
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        // Check if category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }
        
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToDTO);
    }

    /**
     * Convert Product entity to ProductDTO
     * @param product The Product entity
     * @return The ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setSku(product.getSku());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    /**
     * Update Product entity from ProductDTO
     * @param product The Product entity to update
     * @param productDTO The ProductDTO with new data
     * @param category The Category entity
     */
    private void updateProductFromDTO(Product product, ProductDTO productDTO, Category category) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setSku(productDTO.getSku());
        product.setCategory(category);
    }
}
