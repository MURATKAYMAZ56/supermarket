package com.supermarket_assignment.supermarket.controller;

import com.supermarket_assignment.supermarket.controller.response.ProductDto;
import com.supermarket_assignment.supermarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping()
    public void submit(@RequestParam("file") MultipartFile file) {
         productService.uploadFile(file);
    }

    @GetMapping()
    public List<ProductDto> getAll() {
        return productService.findAllSortedByExpiryDate()
                .stream()
                .map(item -> {
                    ProductDto productDto = new ProductDto();
                    productDto.setBarcode(item.getBarcode());
                    productDto.setCategory(item.getCategory());
                    return productDto;
                }).collect(Collectors.toList());

    }

    @GetMapping("/{barcode}")
    public ProductDto getDataByBarcode(@PathVariable Long barcode){
        return productService.findByBarcode(barcode).map(item -> {
            ProductDto productDto = new ProductDto();
            productDto.setBarcode(item.getBarcode());
            return productDto;
        }).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found!"));
    }
    @DeleteMapping("/{barcode}")
    public void deleteByBarcode(@PathVariable Long barcode){
       productService.deleteByBarcode(barcode);
    }
}
