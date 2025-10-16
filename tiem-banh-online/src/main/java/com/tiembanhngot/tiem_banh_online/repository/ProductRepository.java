package com.tiembanhngot.tiem_banh_online.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tiembanhngot.tiem_banh_online.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findByIsAvailableTrueOrderByCreatedAtDesc();

    boolean existsByCategoryCategoryId(Integer categoryId);

    @Query("SELECT p FROM Product p WHERE p.isAvailable = true AND " +
            "(LOWER(p.name) LIKE LOWER(concat('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(concat('%', :query, '%'))) " +
            "ORDER BY p.name ASC")
    List<Product> searchAvailableProducts(@Param("query") String query);

    List<Product> findTop4ByIsAvailableTrueOrderByCreatedAtDesc();

}
