package com.example.productmanagementapi.repository;

import com.example.productmanagementapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
