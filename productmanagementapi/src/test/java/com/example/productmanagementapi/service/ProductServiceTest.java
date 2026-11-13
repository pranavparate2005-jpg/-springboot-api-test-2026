package com.example.productmanagementapi.service;


import com.example.productmanagementapi.dto.ProductRequestDTO;
import com.example.productmanagementapi.dto.ProductResponseDTO;
import com.example.productmanagementapi.entity.Category;
import com.example.productmanagementapi.entity.Product;
import com.example.productmanagementapi.exception.CategoryNotFoundException;
import com.example.productmanagementapi.exception.ProductNotFoundException;
import com.example.productmanagementapi.repository.CategoryRepository;
import com.example.productmanagementapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void findById_shouldReturnProductDTO(){

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(10L);
        product.setName("Keyboard");
        product.setBrand("Logitech");
        product.setPrice(1500.0);
        product.setCategory(category);

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        ProductResponseDTO result = productService.findById(10L);

        assertEquals(10L, result.getId());
        assertEquals("Keyboard", result.getName());
        assertEquals("Logitech", result.getBrand());
        assertEquals(1500.0, result.getPrice());
        assertEquals("Electronics", result.getCategory().getName());


    }

    @Test
    void findById_shouldThrowExceptionWhenProductNotFound(){
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.findById(999L)
        );
    }

    @Test
    void deleteById_shouldDeleteProductWhenFound(){

        Product product = new Product();
        product.setId(10L);
        product.setName("Keyboard");

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        productService.deleteById(10L);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteById_shouldNotDeleteWhenProductNotFound() {

        // Arrange
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteById(999L)
        );

        // Verify
        verify(productRepository, never()).delete(
                org.mockito.ArgumentMatchers.any(Product.class)
        );
    }

    @Test
    void deleteById_shouldPassCorrectProductToRepository(){

        Product product = new Product();
        product.setId(10L);
        product.setName("Keyboard");

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        productService.deleteById(10L);

        ArgumentCaptor<Product> captor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository, times(1))
                .delete(captor.capture());

        Product capturedProduct = captor.getValue();

        assertEquals(10L, capturedProduct.getId());
        assertEquals("Keyboard", capturedProduct.getName());
    }


    @Test
    void saveProduct_shouldReturnProductResponseDTO() {

        // Arrange

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Mouse");
        request.setBrand("Logitech");
        request.setPrice(800.0);
        request.setQuantity(10);
        request.setCategoryId(1L);

        Product savedProduct = new Product();
        savedProduct.setId(20L);
        savedProduct.setName("Mouse");
        savedProduct.setBrand("Logitech");
        savedProduct.setPrice(800.0);
        savedProduct.setQuantity(10);
        savedProduct.setCategory(category);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // Act

        ProductResponseDTO result =
                productService.saveProduct(request);

        // Assert

        assertEquals(20L, result.getId());
        assertEquals("Mouse", result.getName());
        assertEquals("Logitech", result.getBrand());
        assertEquals(800.0, result.getPrice());
        assertEquals("Electronics", result.getCategory().getName());
    }


    @Test
    void updateProduct_shouldReturnUpdatedProductDTO() {

        // Arrange

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product existingProduct = new Product();
        existingProduct.setId(10L);
        existingProduct.setName("Old Mouse");
        existingProduct.setBrand("HP");
        existingProduct.setPrice(500.0);
        existingProduct.setCategory(category);

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Wireless Mouse");
        request.setBrand("Logitech");
        request.setPrice(1200.0);
        request.setQuantity(15);
        request.setCategoryId(1L);

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(existingProduct));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act

        ProductResponseDTO result =
                productService.updateProduct(10L, request);

        // Assert

        assertEquals(10L, result.getId());
        assertEquals("Wireless Mouse", result.getName());
        assertEquals("Logitech", result.getBrand());
        assertEquals(1200.0, result.getPrice());
        assertEquals("Electronics", result.getCategory().getName());
    }


    @Test
    void searchByName_shouldReturnMatchingProductDTOs() {

        // Arrange

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Wireless Mouse");
        product1.setBrand("Logitech");
        product1.setPrice(1200.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Gaming Mouse");
        product2.setBrand("HP");
        product2.setPrice(900.0);

        List<Product> products = List.of(product1, product2);

        when(productRepository.findByNameContaining("Mouse"))
                .thenReturn(products);

        // Act

        List<ProductResponseDTO> result =
                productService.searchByName("Mouse");

        // Assert

        assertEquals(2, result.size());

        assertEquals("Wireless Mouse", result.get(0).getName());
        assertEquals("Logitech", result.get(0).getBrand());

        assertEquals("Gaming Mouse", result.get(1).getName());
        assertEquals("HP", result.get(1).getBrand());
    }


    @Test
    void searchByName_shouldReturnEmptyListWhenNoProductFound() {

        // Arrange
        when(productRepository.findByNameContaining("Laptop"))
                .thenReturn(List.of());

        // Act
        List<ProductResponseDTO> result =
                productService.searchByName("Laptop");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void getExpensiveProducts_shouldReturnProductDTOs() {

        // Arrange

        Product product = new Product();
        product.setId(5L);
        product.setName("Laptop");
        product.setBrand("Dell");
        product.setPrice(55000.0);

        when(productRepository.getExpensiveProducts(40000.0))
                .thenReturn(List.of(product));

        // Act

        List<ProductResponseDTO> result =
                productService.getExpensiveProducts(40000.0);

        // Assert

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Dell", result.get(0).getBrand());
        assertEquals(55000.0, result.get(0).getPrice());
    }


    @Test
    void getAllProducts_shouldReturnPaginatedProductDTOs() {

        // Arrange

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setBrand("Dell");
        product.setPrice(55000.0);

        List<Product> products = List.of(product);

        Pageable pageable = PageRequest.of(
                0, 5, Sort.by(Sort.Direction.ASC, "name")
        );

        Page<Product> productPage =
                new PageImpl<>(products, pageable, 1);

        when(productRepository.findAll(any(Pageable.class)))
                .thenReturn(productPage );

        // Act

        Page<ProductResponseDTO> result =
                productService.getAllProducts(0, 5, "name", "ASC");

        // Assert

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Laptop", result.getContent().get(0).getName());
        assertEquals("Dell", result.getContent().get(0).getBrand());
    }


    @Test
    void getProductsByCategory_shouldReturnCategoryProducts() {

        // Arrange

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(10L);
        product.setName("Laptop");
        product.setBrand("Dell");
        product.setPrice(55000.0);
        product.setCategory(category);

        Pageable pageable = PageRequest.of(
                0, 5, Sort.by(Sort.Direction.ASC, "name")
        );

        Page<Product> productPage =
                new PageImpl<>(List.of(product), pageable, 1);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.findByCategoryId(
                eq(1L), any(Pageable.class)))
                .thenReturn(productPage);

        // Act

        Page<ProductResponseDTO> result =
                productService.getProductsByCategory(
                        1L, 0, 5, "name", "asc"
                );

        // Assert

        assertEquals(1, result.getTotalElements());
        assertEquals("Laptop", result.getContent().get(0).getName());
        assertEquals("Dell", result.getContent().get(0).getBrand());
        assertEquals("Electronics",
                result.getContent().get(0).getCategory().getName());
    }


    @Test
    void getProductsByCategory_shouldThrowExceptionWhenCategoryNotFound() {

        // Arrange

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                CategoryNotFoundException.class,
                () -> productService.getProductsByCategory(
                        999L, 0, 5, "name", "asc"
                )
        );

        // Verify

        verify(productRepository, never())
                .findByCategoryId(
                        eq(999L), any(Pageable.class)
                );
    }


    @Test
    void updateProduct_shouldThrowExceptionWhenProductNotFound() {

        // Arrange

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Wireless Mouse");
        request.setBrand("Logitech");
        request.setPrice(1200.0);
        request.setQuantity(10);
        request.setCategoryId(1L);

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(999L, request)
        );

        // Verify

        verify(categoryRepository, never()).findById(1L);

        verify(productRepository, never()).save(any(Product.class));
    }

}
