package com.inventory.stockmanagementapi.repository;

import com.inventory.stockmanagementapi.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find all products belonging to a specific category
     * @param categoryId The category id
     * @return A list of products in the category
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * Find all products belonging to a specific category with pagination
     * @param categoryId The category id
     * @param pageable Pagination information
     * @return A page of products in the category
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * Find a product by its SKU
     * @param sku The product SKU
     * @return An optional containing the product if found
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Check if a product with the given SKU exists
     * @param sku The product SKU
     * @return True if a product with the SKU exists, false otherwise
     */
    boolean existsBySku(String sku);
    
    /**
     * Find products with low stock (quantity below threshold)
     * @param threshold The threshold quantity
     * @return A list of products with low stock
     */
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
    
    /**
     * Search products by name or description containing the search term
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return A page of matching products
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);
}
