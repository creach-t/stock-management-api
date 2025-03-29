package com.inventory.stockmanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "Stock Management API",
        version = "0.1.0",
        description = "API REST de gestion de stock évolutive et modulaire"
    )
)
public class StockManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockManagementApiApplication.class, args);
    }
}
