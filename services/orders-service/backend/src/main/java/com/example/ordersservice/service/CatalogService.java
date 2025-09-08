package com.example.ordersservice.service;

import com.example.ordersservice.model.Catalog;
import com.example.ordersservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CatalogService {

    private final CatalogRepository catalogRepository;

    /**
     * Fetch all catalogs
     */
    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    /**
     * Fetch catalog by ID
     */
    public Catalog getCatalogById(Long id) {
        return catalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalog not found with id: " + id));
    }

    /**
     * Fetch catalog by name
     */
    public Catalog getCatalogByName(String name) {
        Catalog catalog = catalogRepository.findByCatalogNameIgnoreCase(name);
        if (catalog == null) {
            throw new RuntimeException("Catalog not found with name: " + name);
        }
        return catalog;
    }

    /**
     * Create a new catalog
     */
    public Catalog createCatalog(Catalog catalog) {
        if (catalogRepository.existsByCatalogNameIgnoreCase(catalog.getCatalogName())) {
            throw new RuntimeException("Catalog already exists with name: " + catalog.getCatalogName());
        }
        return catalogRepository.save(catalog);
    }

    /**
     * Update an existing catalog
     */
    public Catalog updateCatalog(Long id, Catalog catalogDetails) {
        Catalog catalog = getCatalogById(id);

        catalog.setCatalogName(catalogDetails.getCatalogName());
        catalog.setCatalogDescription(catalogDetails.getCatalogDescription());

        return catalogRepository.save(catalog);
    }

    /**
     * Delete a catalog
     */
    public void deleteCatalog(Long id) {
        if (!catalogRepository.existsById(id)) {
            throw new RuntimeException("Catalog not found with id: " + id);
        }
        catalogRepository.deleteById(id);
    }
}
