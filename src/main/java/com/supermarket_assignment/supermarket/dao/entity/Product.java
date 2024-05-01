package com.supermarket_assignment.supermarket.dao.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Product {

        @Id
        private Long barcode;
        private String category;
        private String itemName;
        private BigDecimal sellingPrice;
        private LocalDate manufacturingDate;
        private LocalDate expiryDate;
        private  int quantity;


}
