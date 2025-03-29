package com.inventory.stockmanagementapi.repository;

import com.inventory.stockmanagementapi.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find a category by its name
     * @param name The category name
     * @return An optional containing the category if found
     */
    Optional<Category> findByName(String name);
    
    /**
     * Check if a category exists by its name
     * @param name The category name
     * @return True if the category exists, false otherwise
     */
    boolean existsByName(String name);
}
