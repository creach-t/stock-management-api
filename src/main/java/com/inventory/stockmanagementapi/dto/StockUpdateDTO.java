package com.inventory.stockmanagementapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateDTO {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Quantity change is required")
    private Integer quantityChange;
    
    @NotNull(message = "Operation type is required")
    private OperationType operationType;
    
    private String notes;
    
    public enum OperationType {
        ADD,        // Add to stock
        REMOVE,     // Remove from stock 
        SET         // Set absolute value
    }
}
