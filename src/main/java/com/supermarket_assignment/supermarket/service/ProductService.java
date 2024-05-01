package com.supermarket_assignment.supermarket.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.supermarket_assignment.supermarket.dao.entity.Product;
import com.supermarket_assignment.supermarket.dao.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public void uploadFile(MultipartFile file) {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .build()) {
            List<String[]> data = reader.readAll();
            productRepository.saveAll(data.stream().map(row -> {
                Product product = new Product();
                product.setBarcode(Long.parseLong(row[0]));
                product.setCategory(row[1]);
                product.setItemName(row[2]);
                product.setSellingPrice(new BigDecimal(row[3]));
                product.setManufacturingDate(LocalDate.parse(row[4], DateTimeFormatter.ISO_LOCAL_DATE));
                product.setExpiryDate(LocalDate.parse(row[5], DateTimeFormatter.ISO_LOCAL_DATE));
                product.setQuantity(Integer.parseInt(row[6]));
                return product;
            }).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> findByBarcode(Long barcode) {
        return productRepository.findById(barcode);
    }

    public List<Product> findAllSortedByExpiryDate() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "expiryDate"));
    }

    public void deleteByBarcode(Long barcode) {
        productRepository.deleteById(barcode);
    }
}
