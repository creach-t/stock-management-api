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

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Bean
    @Profile("!prod") // Only run this initializer if not in production mode
    public CommandLineRunner initData() {
        return args -> {
            initializeData();
        };
    }

    public void initializeData() {
        // Supprimer toutes les données existantes d'abord
        productRepository.deleteAll();
        categoryRepository.deleteAll();

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
        
        Category sports = new Category();
        sports.setName("Sports & Outdoors");
        sports.setDescription("Equipment and clothing for sports and outdoor activities");
        
        Category beauty = new Category();
        beauty.setName("Beauty & Personal Care");
        beauty.setDescription("Cosmetics, skincare, and personal care products");
        
        List<Category> categories = Arrays.asList(electronics, clothing, food, office, sports, beauty);
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
        
        Product tablet = new Product();
        tablet.setName("Super Tablet 12");
        tablet.setDescription("12-inch tablet with high-resolution display");
        tablet.setPrice(new BigDecimal("499.99"));
        tablet.setQuantity(40);
        tablet.setSku("ELEC-TB-004");
        tablet.setCategory(electronics);
        
        Product smartwatch = new Product();
        smartwatch.setName("Fitness Watch Pro");
        smartwatch.setDescription("Smartwatch with health monitoring features");
        smartwatch.setPrice(new BigDecimal("249.99"));
        smartwatch.setQuantity(60);
        smartwatch.setSku("ELEC-SW-005");
        smartwatch.setCategory(electronics);
        
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
        
        Product jacket = new Product();
        jacket.setName("Winter Jacket");
        jacket.setDescription("Warm winter jacket with water-resistant outer layer");
        jacket.setPrice(new BigDecimal("89.99"));
        jacket.setQuantity(75);
        jacket.setSku("CLO-WJ-003");
        jacket.setCategory(clothing);
        
        Product dress = new Product();
        dress.setName("Summer Dress");
        dress.setDescription("Light and flowy summer dress");
        dress.setPrice(new BigDecimal("39.99"));
        dress.setQuantity(100);
        dress.setSku("CLO-SD-004");
        dress.setCategory(clothing);
        
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
        
        Product tea = new Product();
        tea.setName("Herbal Tea Collection");
        tea.setDescription("Assorted herbal teas, 40 bags");
        tea.setPrice(new BigDecimal("8.99"));
        tea.setQuantity(120);
        tea.setSku("FOOD-TE-003");
        tea.setCategory(food);
        
        Product olive = new Product();
        olive.setName("Extra Virgin Olive Oil");
        olive.setDescription("500ml bottle of premium extra virgin olive oil");
        olive.setPrice(new BigDecimal("15.99"));
        olive.setQuantity(60);
        olive.setSku("FOOD-OL-004");
        olive.setCategory(food);
        
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
        
        Product organizer = new Product();
        organizer.setName("Desk Organizer");
        organizer.setDescription("Multi-compartment desk organizer");
        organizer.setPrice(new BigDecimal("24.99"));
        organizer.setQuantity(40);
        organizer.setSku("OFF-DO-004");
        organizer.setCategory(office);
        
        // Sports products
        Product yogaMat = new Product();
        yogaMat.setName("Premium Yoga Mat");
        yogaMat.setDescription("Non-slip surface yoga mat, 6mm thick");
        yogaMat.setPrice(new BigDecimal("34.99"));
        yogaMat.setQuantity(80);
        yogaMat.setSku("SPO-YM-001");
        yogaMat.setCategory(sports);
        
        Product tennisRacket = new Product();
        tennisRacket.setName("Professional Tennis Racket");
        tennisRacket.setDescription("Carbon fiber tennis racket for advanced players");
        tennisRacket.setPrice(new BigDecimal("149.99"));
        tennisRacket.setQuantity(30);
        tennisRacket.setSku("SPO-TR-002");
        tennisRacket.setCategory(sports);
        
        Product runningShoes = new Product();
        runningShoes.setName("Running Shoes");
        runningShoes.setDescription("Lightweight running shoes with cushioned insole");
        runningShoes.setPrice(new BigDecimal("79.99"));
        runningShoes.setQuantity(60);
        runningShoes.setSku("SPO-RS-003");
        runningShoes.setCategory(sports);
        
        // Beauty products
        Product faceCream = new Product();
        faceCream.setName("Hydrating Face Cream");
        faceCream.setDescription("Daily moisturizer for all skin types");
        faceCream.setPrice(new BigDecimal("24.99"));
        faceCream.setQuantity(90);
        faceCream.setSku("BEA-FC-001");
        faceCream.setCategory(beauty);
        
        Product shampoo = new Product();
        shampoo.setName("Volumizing Shampoo");
        shampoo.setDescription("Shampoo for fine hair, 350ml");
        shampoo.setPrice(new BigDecimal("14.99"));
        shampoo.setQuantity(120);
        shampoo.setSku("BEA-SH-002");
        shampoo.setCategory(beauty);
        
        Product lipstick = new Product();
        lipstick.setName("Long-Lasting Lipstick");
        lipstick.setDescription("Matte finish lipstick in various shades");
        lipstick.setPrice(new BigDecimal("17.99"));
        lipstick.setQuantity(100);
        lipstick.setSku("BEA-LS-003");
        lipstick.setCategory(beauty);
        
        // Save all products
        List<Product> products = Arrays.asList(
                smartphone, laptop, headphones, tablet, smartwatch,
                tShirt, jeans, jacket, dress,
                coffee, chocolate, tea, olive,
                notebook, pen, stapler, organizer,
                yogaMat, tennisRacket, runningShoes,
                faceCream, shampoo, lipstick);
        
        productRepository.saveAll(products);
        
        System.out.println("Sample data initialized successfully!");
    }
}
