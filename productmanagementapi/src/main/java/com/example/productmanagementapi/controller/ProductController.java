package com.example.productmanagementapi.controller;


import com.example.productmanagementapi.dto.ProductRequestDTO;
import com.example.productmanagementapi.dto.ProductResponseDTO;
import com.example.productmanagementapi.entity.Product;
import com.example.productmanagementapi.response.ErrorResponse;
import com.example.productmanagementapi.service.ProductService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Product APIs",
        description = "APIs for managing products"
)
@RestController
@RequestMapping("/products")
public class  ProductController {

    @Autowired
    private ProductService productService;

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product and saves it to the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid product data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    additionalProperties = Schema.AdditionalPropertiesValue.TRUE
                            ),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "name": "Product name is required",
                                  "price": "Price must be greater than 0",
                                  "quantity": "Quantity cannot be negative"
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
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product details to create",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    examples = @ExampleObject(
                            name = "Product Example",
                            value = """
                                    {
                                     "name" : "Gaming Laptop",
                                     "brand" : "Asus",
                                     "price" : 80000,
                                     "quantity" : 10,
                                     "categoryId" : 1}
                                    """
                    )
            )
    )
    @PostMapping
    public ResponseEntity<ProductResponseDTO> saveProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){

        ProductResponseDTO savedProduct = productService.saveProduct(productRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProduct);
    }

    // #######################################################################################

    @Operation(
            summary = "Get all products",
            description = "Fetches all products with pagination and sorting"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination or sorting parameters"
            )
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @Parameter(
                    description = "Page number starting from 0",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of products per page",
                    example = "3"
            )
            @RequestParam(defaultValue = "3") int size,

            @Parameter(
                    description = "Field used for sorting products",
                    example = "id"
            )
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(
                    description = "Sorting direction: asc or desc",
                    example = "asc"
            )
            @RequestParam(defaultValue = "asc") String direction){

        return ResponseEntity.ok(productService.getAllProducts(page, size, sortBy, direction));
    }

    // #######################################################################################

    @Operation(
            summary = "Get product by ID",
            description = "Fetch a product using its unique ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(
            @Parameter(
                    description = "Unique ID of the product",
                    example = "1"
            ) @PathVariable Long id){
        return ResponseEntity.ok(productService.findById(id));
    }

    // #######################################################################################

    @Operation(
            summary = "Delete product",
            description = "Deletes an existing product using its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Product deleted successfully"
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(
                    description = "Unique ID of the product",
                    example = "1"
            )
            @PathVariable Long id){

        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // #######################################################################################

    @Operation(
            summary = "Update product",
            description = "Updates an existing product using its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid product data",
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
                                          "name": "Product name is required",
                                          "price": "Price must be greater than 0",
                                          "quantity": "Quantity cannot be negative"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found or category not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(
                    description = "Unique ID of the product",
                    example = "1"
            )
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO productRequestDTO){

        ProductResponseDTO updateProduct = productService.updateProduct(id, productRequestDTO);

        return ResponseEntity.ok(updateProduct);
    }

    // #######################################################################################

    @Operation(
            summary =  "Search product by name",
            description = "Fetch products by searching their name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products found successfully"
    )
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchByName(
            @Parameter(
                    description = "Name or keyword to search for a product",
                    example = "Laptop"
            )
            @RequestParam String name){

        return ResponseEntity.ok(productService.searchByName(name));
    }

    // #######################################################################################

    @Operation(
            summary = "Get Expensive Product First",
            description = "Fetch products whose price is greater than the specified price"
    )
    @ApiResponse(
                    responseCode = "200",
                    description = "Products fetched successfully"
            )

    @GetMapping("/expensive")
    public ResponseEntity<List<ProductResponseDTO>> getExpensiveProducts(
            @Parameter(
                    description = "Minimum price to filter products",
                    example = "50000"
            )
            @RequestParam Double price){

        return ResponseEntity.ok(productService.getExpensiveProducts(price));
    }

    // #######################################################################################

    @Operation(
            summary = "Get product by category ID",
            description = "Fetch products belonging to a specific category with pagination and sorting"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products fetched successfully"
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
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @Parameter(
                    description = "Unique ID of the category",
                    example = "1"
            )
            @PathVariable Long categoryId,
            @Parameter(
                    description = "Page number starting from 0",
                    example = "0"
            )
            @RequestParam (defaultValue = "0") int page,
            @Parameter(
                    description = "Number of products per page",
                    example = "3"
            )
            @RequestParam (defaultValue = "3") int size,
            @Parameter(
                    description = "Field used for sorting products",
                    example = "price"
            )
            @RequestParam (defaultValue = "id") String sortBy,
            @Parameter(
                    description = "Sorting direction: asc or desc",
                    example = "asc"
            )
            @RequestParam (defaultValue = "asc") String direction) {

        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, page, size, sortBy, direction)
        );
    }
}
