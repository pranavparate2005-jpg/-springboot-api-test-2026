package com.example.productmanagementapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    @Schema(
            description = "Unique ID of the category",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the category",
            example = "Electronics"
    )
    private String name;
}
