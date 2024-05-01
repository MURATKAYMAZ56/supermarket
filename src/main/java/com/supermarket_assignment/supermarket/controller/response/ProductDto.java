package com.supermarket_assignment.supermarket.controller.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductDto {
    private Long barcode;
    private String category;
    private String itemName;
    private BigDecimal sellingPrice;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private  int quantity;
}
