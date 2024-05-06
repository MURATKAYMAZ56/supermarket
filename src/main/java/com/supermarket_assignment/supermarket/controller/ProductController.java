package com.supermarket_assignment.supermarket.controller;

import com.supermarket_assignment.supermarket.controller.response.ProductDto;
import com.supermarket_assignment.supermarket.dao.entity.Product;
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
    public void importFile(@RequestParam("file") MultipartFile file) {
        productService.uploadFile(file);
    }

    @GetMapping()
    public List<ProductDto> getAll() {
        return productService.findAllSortedByExpiryDate()
                .stream()
                .map(this::toProductDto).collect(Collectors.toList());
    }

    @GetMapping("/{barcode}")
    public ProductDto getDataByBarcode(@PathVariable Long barcode) {
        return productService.findByBarcode(barcode).map(this::toProductDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found!"));
    }

    @DeleteMapping("/{barcode}")
    public void deleteByBarcode(@PathVariable Long barcode) {
        productService.deleteByBarcode(barcode);
    }

    private ProductDto toProductDto(Product item){
        ProductDto productDto = new ProductDto();
        productDto.setBarcode(item.getBarcode());
        productDto.setCategory(item.getCategory());
        productDto.setItemName(item.getItemName());
        productDto.setSellingPrice(item.getSellingPrice());
        productDto.setManufacturingDate(item.getManufacturingDate());
        productDto.setExpiryDate(item.getExpiryDate());
        productDto.setQuantity(item.getQuantity());
        return productDto;

    }
}
