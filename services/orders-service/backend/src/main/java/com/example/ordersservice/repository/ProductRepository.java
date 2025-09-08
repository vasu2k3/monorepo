package com.example.ordersservice.repository;

import com.example.ordersservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by catalog id
    List<Product> findByCatalog_CatalogId(Long catalogId);

    // Search by name (case insensitive)
    List<Product> findByProductNameContainingIgnoreCase(String productName);
}
