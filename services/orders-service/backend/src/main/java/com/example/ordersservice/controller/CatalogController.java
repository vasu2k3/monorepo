package com.example.ordersservice.controller;

import com.example.ordersservice.model.Catalog;
import com.example.ordersservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/catalogs")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    /**
     * Get all catalogs
     */
    @GetMapping
    public ResponseEntity<List<Catalog>> getAllCatalogs() {
        return ResponseEntity.ok(catalogService.getAllCatalogs());
    }

    /**
     * Get catalog by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Catalog> getCatalogById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getCatalogById(id));
    }

    /**
     * Get catalog by name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Catalog> getCatalogByName(@PathVariable String name) {
        return ResponseEntity.ok(catalogService.getCatalogByName(name));
    }

    /**
     * Create a new catalog
     */
    @PostMapping
    public ResponseEntity<Catalog> createCatalog(@RequestBody Catalog catalog) {
        return ResponseEntity.ok(catalogService.createCatalog(catalog));
    }

    /**
     * Update an existing catalog
     */
    @PutMapping("/{id}")
    public ResponseEntity<Catalog> updateCatalog(@PathVariable Long id, @RequestBody Catalog catalogDetails) {
        return ResponseEntity.ok(catalogService.updateCatalog(id, catalogDetails));
    }

    /**
     * Delete a catalog
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCatalog(@PathVariable Long id) {
        catalogService.deleteCatalog(id);
        return ResponseEntity.ok("Catalog deleted successfully with ID: " + id);
    }
}
