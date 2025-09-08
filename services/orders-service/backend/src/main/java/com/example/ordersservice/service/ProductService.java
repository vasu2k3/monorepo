package com.example.ordersservice.service;

import com.example.ordersservice.model.Product;
import com.example.ordersservice.model.Catalog;
import com.example.ordersservice.repository.ProductRepository;
import com.example.ordersservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CatalogRepository catalogRepository;

    /**
     * Fetch all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Fetch a product by its ID
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Fetch products by Catalog ID
     */
    public List<Product> getProductsByCatalogId(Long catalogId) {
        return productRepository.findByCatalog_CatalogId(catalogId);
    }

    /**
     * Create a new product
     */
    public Product createProduct(Product product, Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new RuntimeException("Catalog not found with id: " + catalogId));
        product.setCatalog(catalog);
        return productRepository.save(product);
    }

    /**
     * Update an existing product
     */
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        product.setProductName(productDetails.getProductName());
        product.setProductDescription(productDetails.getProductDescription());
        product.setProductPrice(productDetails.getProductPrice());
        product.setProductStock(productDetails.getProductStock());

        return productRepository.save(product);
    }

    /**
     * Delete a product
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
