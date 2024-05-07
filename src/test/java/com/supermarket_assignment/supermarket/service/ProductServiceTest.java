package com.supermarket_assignment.supermarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket_assignment.supermarket.controller.ProductController;
import com.supermarket_assignment.supermarket.dao.entity.Product;
import com.supermarket_assignment.supermarket.dao.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testFindByBarcode() {
        Product product = buildProduct();
        Mockito.when(productService.findByBarcode(Mockito.any())).thenReturn(Optional.of(product));

        Optional<Product> result = productRepository.findById(Long.valueOf(156980L));

        Assertions.assertEquals("Bakery", result.get().getCategory());
        Assertions.assertEquals("Bread", result.get().getItemName());
        Assertions.assertEquals(BigDecimal.valueOf(1.99), result.get().getSellingPrice());
        Assertions.assertEquals(LocalDate.of(2023, 01, 02), result.get().getManufacturingDate());
        Assertions.assertEquals(LocalDate.of(2023, 01, 10), result.get().getExpiryDate());
        Assertions.assertEquals(150, result.get().getQuantity());
    }
    @Test
    void testfindAllSortedByExpireDate(){
        // TODO: SortList
        List<Product> productList = List.of(buildProduct());
        Mockito.when(productService.findAllSortedByExpiryDate()).thenReturn(productList);
        List<Product> result = productRepository.findAll();
    }


    private Product buildProduct() {
        Product product = new Product();
        product.setBarcode(156980L);
        product.setCategory("Bakery");
        product.setItemName("Bread");
        product.setSellingPrice(BigDecimal.valueOf(1.99));
        product.setManufacturingDate(LocalDate.parse("2023-01-02"));
        product.setExpiryDate(LocalDate.parse("2023-01-10"));
        product.setQuantity(150);
        return product;
    }

}
