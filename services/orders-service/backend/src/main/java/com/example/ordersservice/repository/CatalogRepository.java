package com.example.ordersservice.repository;

import com.example.ordersservice.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    // Find catalog by its name
    Catalog findByCatalogNameIgnoreCase(String catalogName);

    // Check if catalog with the given name exists
    boolean existsByCatalogNameIgnoreCase(String catalogName);
}
