package com.example.productmanagementapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProductResponseDTO {

    @Schema(
            description = "Unique ID of the product",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the product",
            example = "Gaming Laptop"
    )
    private String name;

    @Schema(
            description = "Brand of the product",
            example = "Asus"
    )
    private String brand;


    @Schema(
            description = "Price of the product",
            example = "80000.0"
    )
    private Double price;

    @Schema(
            description = "Category details of the product"
    )
    private CategoryResponseDTO category;

    public ProductResponseDTO(){

    }

    public CategoryResponseDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryResponseDTO category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
