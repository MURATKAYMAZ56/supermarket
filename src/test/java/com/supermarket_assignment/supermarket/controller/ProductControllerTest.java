package com.supermarket_assignment.supermarket.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket_assignment.supermarket.dao.entity.Product;
import com.supermarket_assignment.supermarket.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @Test
    void importFile_whenValidInput_thenReturns200() throws Exception {
        MockMultipartFile file  = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("Barcode,Category,ItemName,SellingPrice,ManufacturingDate,ExpiryDate,Quantity\t\n" +
                        "75930,Dairy,Milk,2.99,2023-01-01,2023-01-15,100\n" +
                        "84035,Dairy,Yogurt,1.79,2023-02-05,2023-02-25,80").getBytes()
        );
        mockMvc.perform(multipart("/api/products").file(file))
                .andExpect(status().isOk());

    }

    @Test
    void testGetAll_whenValidInput_thenReturns200() throws Exception {
        List<Product> productList = List.of(buildProduct());
        Mockito.when(productService.findAllSortedByExpiryDate()).thenReturn(productList);

        MvcResult mvcResult = mockMvc.perform(get("/api/products")
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(productList), actualResponseBody);

    }

    @Test
    void testGetDataByBarcode_whenValidInput_thenReturns200() throws Exception {
        Product product = buildProduct();
        Mockito.when(productService.findByBarcode(Mockito.any())).thenReturn(Optional.of(product));
        MvcResult mvcResult = mockMvc.perform(get("/api/products/{barcode}", 156980)
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(product), actualResponseBody);

    }

    @Test
    void testGetDataByBarcode_whenDataNotFound_thenThrows404() throws Exception {
        Mockito.when(productService.findByBarcode(Mockito.any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/products/{barcode}", 156980)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
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
