package com.inventory.stockmanagementapi.config;

import com.inventory.stockmanagementapi.domain.Category;
import com.inventory.stockmanagementapi.domain.Product;
import com.inventory.stockmanagementapi.repository.CategoryRepository;
import com.inventory.stockmanagementapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Profile("!prod") // Only run this initializer if not in production mode
    public CommandLineRunner initData(
            @Autowired CategoryRepository categoryRepository,
            @Autowired ProductRepository productRepository) {
        
        return args -> {
            // Create categories
            Category electronics = new Category();
            electronics.setName("Electronics");
            electronics.setDescription("Electronic devices and accessories");
            
            Category clothing = new Category();
            clothing.setName("Clothing");
            clothing.setDescription("Apparel and fashion items");
            
            Category food = new Category();
            food.setName("Food & Beverages");
            food.setDescription("Consumable food and drink products");
            
            Category office = new Category();
            office.setName("Office Supplies");
            office.setDescription("Supplies and equipment for office use");
            
            List<Category> categories = Arrays.asList(electronics, clothing, food, office);
            categoryRepository.saveAll(categories);
            
            // Create products
            // Electronics products
            Product smartphone = new Product();
            smartphone.setName("Smartphone XYZ");
            smartphone.setDescription("Latest model with advanced features");
            smartphone.setPrice(new BigDecimal("699.99"));
            smartphone.setQuantity(50);
            smartphone.setSku("ELEC-SP-001");
            smartphone.setCategory(electronics);
            
            Product laptop = new Product();
            laptop.setName("Ultra Laptop Pro");
            laptop.setDescription("High-performance laptop for professionals");
            laptop.setPrice(new BigDecimal("1299.99"));
            laptop.setQuantity(25);
            laptop.setSku("ELEC-LP-002");
            laptop.setCategory(electronics);
            
            Product headphones = new Product();
            headphones.setName("Wireless Headphones");
            headphones.setDescription("Noise-cancelling wireless headphones");
            headphones.setPrice(new BigDecimal("199.99"));
            headphones.setQuantity(100);
            headphones.setSku("ELEC-HP-003");
            headphones.setCategory(electronics);
            
            // Clothing products
            Product tShirt = new Product();
            tShirt.setName("Cotton T-Shirt");
            tShirt.setDescription("Comfortable cotton t-shirt in various colors");
            tShirt.setPrice(new BigDecimal("19.99"));
            tShirt.setQuantity(200);
            tShirt.setSku("CLO-TS-001");
            tShirt.setCategory(clothing);
            
            Product jeans = new Product();
            jeans.setName("Denim Jeans");
            jeans.setDescription("Classic denim jeans in multiple sizes");
            jeans.setPrice(new BigDecimal("49.99"));
            jeans.setQuantity(150);
            jeans.setSku("CLO-DJ-002");
            jeans.setCategory(clothing);
            
            // Food products
            Product coffee = new Product();
            coffee.setName("Premium Coffee Beans");
            coffee.setDescription("Organic fair-trade coffee beans");
            coffee.setPrice(new BigDecimal("12.99"));
            coffee.setQuantity(75);
            coffee.setSku("FOOD-CF-001");
            coffee.setCategory(food);
            
            Product chocolate = new Product();
            chocolate.setName("Dark Chocolate Bar");
            chocolate.setDescription("70% cocoa dark chocolate");
            chocolate.setPrice(new BigDecimal("3.99"));
            chocolate.setQuantity(300);
            chocolate.setSku("FOOD-CH-002");
            chocolate.setCategory(food);
            
            // Office supplies
            Product notebook = new Product();
            notebook.setName("Spiral Notebook");
            notebook.setDescription("College-ruled spiral notebook, 100 pages");
            notebook.setPrice(new BigDecimal("4.99"));
            notebook.setQuantity(500);
            notebook.setSku("OFF-NB-001");
            notebook.setCategory(office);
            
            Product pen = new Product();
            pen.setName("Ballpoint Pen Set");
            pen.setDescription("Set of 10 ballpoint pens in various colors");
            pen.setPrice(new BigDecimal("8.99"));
            pen.setQuantity(250);
            pen.setSku("OFF-PEN-002");
            pen.setCategory(office);
            
            Product stapler = new Product();
            stapler.setName("Desktop Stapler");
            stapler.setDescription("Heavy-duty desktop stapler");
            stapler.setPrice(new BigDecimal("12.49"));
            stapler.setQuantity(50);
            stapler.setSku("OFF-ST-003");
            stapler.setCategory(office);
            
            // Save all products
            List<Product> products = Arrays.asList(
                    smartphone, laptop, headphones, 
                    tShirt, jeans, 
                    coffee, chocolate, 
                    notebook, pen, stapler);
            
            productRepository.saveAll(products);
            
            System.out.println("Sample data initialized successfully!");
        };
    }
}
