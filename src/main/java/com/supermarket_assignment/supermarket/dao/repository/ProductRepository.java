package com.supermarket_assignment.supermarket.dao.repository;

import com.supermarket_assignment.supermarket.dao.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long> {
}
