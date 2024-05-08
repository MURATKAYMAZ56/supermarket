package com.supermarket_assignment.supermarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket_assignment.supermarket.controller.ProductController;
import com.supermarket_assignment.supermarket.dao.entity.Product;
import com.supermarket_assignment.supermarket.dao.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    @Captor
    private ArgumentCaptor<List<Product>> argumentCaptorProducts;
    @Test
    void testUploadFile() {
        productService.uploadFile(buildMultipartFile());

        verify(productRepository).saveAll(argumentCaptorProducts.capture());

        assertFalse(argumentCaptorProducts.getValue().isEmpty());
        assertEquals(75930, argumentCaptorProducts.getValue().get(0).getBarcode());
        assertEquals(84035, argumentCaptorProducts.getValue().get(1).getBarcode());
    }

    @Test
    void testUploadFile_whenSaveAllThrowsException() {
        when(productRepository.saveAll(any())).thenThrow(new RuntimeException());
        assertThrows(Exception.class, () -> productService.uploadFile(buildMultipartFile()));
    }
    @Test
    void testFindByBarcode() {
        Product product = buildProduct();
        when(productService.findByBarcode(Mockito.any())).thenReturn(Optional.of(product));

        Optional<Product> result = productRepository.findById(156980L);

        assertEquals("Bakery", result.get().getCategory());
        assertEquals("Bread", result.get().getItemName());
        assertEquals(BigDecimal.valueOf(1.99), result.get().getSellingPrice());
        assertEquals(LocalDate.of(2023, 01, 02), result.get().getManufacturingDate());
        assertEquals(LocalDate.of(2023, 01, 10), result.get().getExpiryDate());
        assertEquals(150, result.get().getQuantity());
    }
    @Test
    void testFindAllSortedByExpireDate() {
        Product product1 = buildProduct();

        Product product2 = buildProduct();
        product2.setBarcode(11111L);
        product2.setExpiryDate(product1.getExpiryDate().minusDays(7));

        ArrayList<Product> productList = new ArrayList<>(0);
        productList.add(product1);
        productList.add(product2);

        // asc sort by expiry date
        productList.sort(Comparator.comparing(Product::getExpiryDate));

        when(productRepository.findAll(any(Sort.class))).thenReturn(productList);

        List<Product> result = productService.findAllSortedByExpiryDate();

        assertEquals(product2.getBarcode(), result.get(0).getBarcode());
    }

    @Test
    void testDeleteByBarcode() {
        productService.deleteByBarcode(1L);
    }

    private MockMultipartFile buildMultipartFile() {
        return new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("Barcode,Category,ItemName,SellingPrice,ManufacturingDate,ExpiryDate,Quantity\t\n" +
                        "75930,Dairy,Milk,2.99,2023-01-01,2023-01-15,100\n" +
                        "84035,Dairy,Yogurt,1.79,2023-02-05,2023-02-25,80").getBytes()
        );
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
