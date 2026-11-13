package com.example.productmanagementapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.examples.Example;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    @Schema(
            description = "Name of the category",
            example = "Electronics"
    )
    @NotBlank(message = "Category name is required")
    private String name;
}
