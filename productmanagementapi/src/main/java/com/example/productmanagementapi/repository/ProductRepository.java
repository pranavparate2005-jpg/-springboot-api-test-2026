package com.example.productmanagementapi.repository;

import com.example.productmanagementapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByName(String name);
    List<Product> findByNameContaining(String name);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);


    //Query
    @Query("SELECT p FROM Product p WHERE p.price > :price")
    List<Product> getExpensiveProducts(@Param("price") Double price);

    @Query("SELECT p FROM Product p WHERE p.brand = :brand")
    List<Product> getProductsByBrand(@Param("brand") String brand);
}
