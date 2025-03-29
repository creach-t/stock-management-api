package com.inventory.stockmanagementapi.config;

import com.inventory.stockmanagementapi.domain.Category;
import com.inventory.stockmanagementapi.domain.Product;
import com.inventory.stockmanagementapi.repository.CategoryRepository;
import com.inventory.stockmanagementapi.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataResetService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    /**
     * Initialise les données au démarrage de l'application
     */
    @PostConstruct
    public void init() {
        resetData();
    }

    /**
     * Réinitialise les données toutes les minutes (60000 ms)
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void resetData() {
        log.info("Réinitialisation des données...");
        
        // Suppression des données existantes
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        
        // Création des catégories
        List<Category> categories = createCategories();
        
        // Création des produits
        createProducts(categories);
        
        log.info("Réinitialisation des données terminée !");
    }

    private List<Category> createCategories() {
        List<Category> categories = Arrays.asList(
                Category.builder().name("Electronics").description("Electronic devices and accessories").build(),
                Category.builder().name("Clothing").description("Clothes and fashion accessories").build(),
                Category.builder().name("Food & Beverages").description("Consumable items").build(),
                Category.builder().name("Office Supplies").description("Items used in offices").build()
        );
        
        return categoryRepository.saveAll(categories);
    }

    private void createProducts(List<Category> categories) {
        Category electronics = categories.get(0);
        Category clothing = categories.get(1);
        Category food = categories.get(2);
        Category office = categories.get(3);

        List<Product> products = Arrays.asList(
                Product.builder()
                        .name("Smartphone X")
                        .description("Latest model with high-end features")
                        .sku("ELEC-SP-001")
                        .price(new BigDecimal("999.99"))
                        .quantity(20)
                        .category(electronics)
                        .build(),
                Product.builder()
                        .name("Laptop Pro")
                        .description("Powerful laptop for professionals")
                        .sku("ELEC-LP-002")
                        .price(new BigDecimal("1499.99"))
                        .quantity(15)
                        .category(electronics)
                        .build(),
                Product.builder()
                        .name("Wireless Headphones")
                        .description("Noise cancelling bluetooth headphones")
                        .sku("ELEC-WH-003")
                        .price(new BigDecimal("199.99"))
                        .quantity(50)
                        .category(electronics)
                        .build(),
                Product.builder()
                        .name("Jeans")
                        .description("Classic blue denim jeans")
                        .sku("CLOTH-J-001")
                        .price(new BigDecimal("59.99"))
                        .quantity(100)
                        .category(clothing)
                        .build(),
                Product.builder()
                        .name("T-Shirt")
                        .description("Cotton t-shirt in various colors")
                        .sku("CLOTH-TS-002")
                        .price(new BigDecimal("19.99"))
                        .quantity(200)
                        .category(clothing)
                        .build(),
                Product.builder()
                        .name("Chocolate Bar")
                        .description("Premium dark chocolate")
                        .sku("FOOD-CH-001")
                        .price(new BigDecimal("3.99"))
                        .quantity(500)
                        .category(food)
                        .build(),
                Product.builder()
                        .name("Coffee Beans")
                        .description("Freshly roasted arabica coffee beans")
                        .sku("FOOD-CF-002")
                        .price(new BigDecimal("15.99"))
                        .quantity(100)
                        .category(food)
                        .build(),
                Product.builder()
                        .name("Notebook")
                        .description("Lined paper notebook")
                        .sku("OFFICE-NB-001")
                        .price(new BigDecimal("4.99"))
                        .quantity(200)
                        .category(office)
                        .build(),
                Product.builder()
                        .name("Pen Set")
                        .description("Set of 5 premium ballpoint pens")
                        .sku("OFFICE-PS-002")
                        .price(new BigDecimal("12.99"))
                        .quantity(150)
                        .category(office)
                        .build(),
                Product.builder()
                        .name("Desk Organizer")
                        .description("Wooden desk organizer with multiple compartments")
                        .sku("OFFICE-DO-003")
                        .price(new BigDecimal("24.99"))
                        .quantity(75)
                        .category(office)
                        .build()
        );

        productRepository.saveAll(products);
    }
}
