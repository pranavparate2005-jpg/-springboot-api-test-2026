
package com.example.productmanagementapi.controller;

import com.example.productmanagementapi.dto.ProductResponseDTO;
import com.example.productmanagementapi.service.JwtService;
import com.example.productmanagementapi.service.ProductService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.example.productmanagementapi.service.CustomUserDetailService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import org.springframework.http.MediaType;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void findById_shouldReturn200AndProduct() throws Exception {

        // Arrange
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(10L);
        response.setName("Laptop");
        response.setBrand("Dell");
        response.setPrice(55000.0);

        when(productService.findById(10L))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.brand").value("Dell"))
                .andExpect(jsonPath("$.price").value(55000.0));
    }

    @Test
    void getAllProducts_shouldReturn200AndProductList() throws Exception {

        // Arrange
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(1L);
        response.setName("Laptop");
        response.setBrand("Dell");
        response.setPrice(55000.0);

        PageImpl<ProductResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(productService.getAllProducts(0, 10, "name", "asc"))
                .thenReturn(page);

        // Act + Assert
        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Laptop"))
                .andExpect(jsonPath("$.content[0].brand").value("Dell"))
                .andExpect(jsonPath("$.content[0].price").value(55000.0));
    }

    @Test
    void saveProduct_withInvalidData_shouldReturn400() throws Exception {

        String invalidJson = """
            {
                "name": "",
                "brand": "",
                "price": -100,
                "categoryId": null
            }
            """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
