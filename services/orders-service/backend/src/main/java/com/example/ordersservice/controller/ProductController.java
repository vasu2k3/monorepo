package com.example.ordersservice.controller;

import com.example.ordersservice.model.Product;
import com.example.ordersservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Get all products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Get products by catalog ID
     */
    @GetMapping("/catalog/{catalogId}")
    public ResponseEntity<List<Product>> getProductsByCatalogId(@PathVariable Long catalogId) {
        return ResponseEntity.ok(productService.getProductsByCatalogId(catalogId));
    }

    /**
     * Create a new product
     */
    @PostMapping("/catalog/{catalogId}")
    public ResponseEntity<Product> createProduct(@PathVariable Long catalogId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product, catalogId));
    }

    /**
     * Update a product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }

    /**
     * Delete a product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully with ID: " + id);
    }
}
