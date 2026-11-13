package com.example.productmanagementapi.service;


import com.example.productmanagementapi.dto.CategoryRequestDTO;
import com.example.productmanagementapi.dto.CategoryResponseDTO;
import com.example.productmanagementapi.dto.ProductResponseDTO;
import com.example.productmanagementapi.entity.Category;
import com.example.productmanagementapi.entity.Product;
import com.example.productmanagementapi.exception.CategoryNotFoundException;
import com.example.productmanagementapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryRequestDTO categoryRequestDTO){

        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        return categoryRepository.save(category);
    }

    public List<CategoryResponseDTO> getAllCategories(){

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryResponseDTO(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }

    public List<ProductResponseDTO> getProductsByCategory(Long categoryId){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found with id " + categoryId)
                );

        return category.getProducts()
                .stream()
                .map(product -> {

                    ProductResponseDTO dto = new ProductResponseDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setBrand(product.getBrand());
                    dto.setPrice(product.getPrice());

                    if(product.getCategory() != null){

                        CategoryResponseDTO categoryDTO = new CategoryResponseDTO();

                        categoryDTO.setId(product.getCategory().getId());
                        categoryDTO.setName(product.getCategory().getName());

                        dto.setCategory(categoryDTO);
                    }

                    return dto;
                }).toList();
    }

    public CategoryResponseDTO getById(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found with id " + id)
                );

        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id)
                );

        existingCategory.setName(categoryRequestDTO.getName());

        Category updatedCategory = categoryRepository.save(existingCategory);

        return new CategoryResponseDTO(
                updatedCategory.getId(),
                updatedCategory.getName()
        );
    }

    public void deleteCategory(Long id){

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id)
                );

        categoryRepository.delete(existingCategory);
    }
}
