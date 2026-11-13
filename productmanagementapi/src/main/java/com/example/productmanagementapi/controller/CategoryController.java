package com.example.productmanagementapi.controller;


import com.example.productmanagementapi.dto.CategoryRequestDTO;
import com.example.productmanagementapi.dto.CategoryResponseDTO;
import com.example.productmanagementapi.dto.ProductResponseDTO;
import com.example.productmanagementapi.entity.Category;
import com.example.productmanagementapi.entity.Product;
import com.example.productmanagementapi.response.ErrorResponse;
import com.example.productmanagementapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Category APIs",
        description = "APIs for managing categories"
)
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Create a new category",
            description = "Creates a new category and saves it to the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    additionalProperties = Schema.AdditionalPropertiesValue.TRUE
                            ),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "name": "Category name is required"
                                }
                                """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){

        Category category = categoryService.createCategory(categoryRequestDTO);
        CategoryResponseDTO response = new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

//#############################################################################################

    @Operation(
            summary = "Get all categories",
            description = "Fetches all categories from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categories fetched successfully"
    )
    @GetMapping
    public List<CategoryResponseDTO> getAllCategories(){

        return categoryService.getAllCategories();
    }

    // #########################################################################################

    @Operation(
            summary = "Get category by ID",
            description = "Fetches a category using its unique ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Category fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public CategoryResponseDTO getById(
            @Parameter(
                    description = "Unique ID of the category",
                    example = "1"
            )
            @PathVariable Long id){

        return categoryService.getById(id);
    }

    // #########################################################################################

    @Operation(
            summary = "Get products by category",
            description = "Fetches all products belonging to a specific category"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Category fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @Parameter(
                    description = "Unique ID of the category",
                    example = "1"
            )
            @PathVariable Long categoryId){

        return ResponseEntity.ok(
                categoryService.getProductsByCategory(categoryId));
    }

    // #########################################################################################

    @Operation(
            summary = "Update category",
            description = "Updates an existing category using its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    additionalProperties =
                                            Schema.AdditionalPropertiesValue.TRUE
                            ),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "name": "Category name is required"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(
            @Parameter(
                    description = "Unique ID of the category",
                    example = "1"
            )
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO){

        return categoryService.updateCategory(id,categoryRequestDTO);
    }

    // #########################################################################################

    @Operation(
            summary = "Delete category",
            description = "Deletes an existing category using its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(
                    description = "Unique ID of the category",
                    example = "1"
            )
            @PathVariable Long id){

        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
