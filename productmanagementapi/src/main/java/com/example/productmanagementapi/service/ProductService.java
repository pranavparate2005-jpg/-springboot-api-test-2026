package com.example.productmanagementapi.service;

import com.example.productmanagementapi.dto.CategoryResponseDTO;
import com.example.productmanagementapi.dto.ProductRequestDTO;
import com.example.productmanagementapi.dto.ProductResponseDTO;
import com.example.productmanagementapi.entity.Category;
import com.example.productmanagementapi.entity.Product;
import com.example.productmanagementapi.exception.CategoryNotFoundException;
import com.example.productmanagementapi.exception.ProductNotFoundException;
import com.example.productmanagementapi.repository.CategoryRepository;
import com.example.productmanagementapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

   public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO){

       Product product = new Product();
       product.setName(productRequestDTO.getName());
       product.setBrand(productRequestDTO.getBrand());
       product.setPrice(productRequestDTO.getPrice());
       product.setQuantity(productRequestDTO.getQuantity());

       Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
               .orElseThrow(() ->
                       new CategoryNotFoundException("Category not found with id " + productRequestDTO.getCategoryId()
                       )
               );

       product.setCategory(category);

       Product savedproduct = productRepository.save(product);

       ProductResponseDTO productResponseDTO = new ProductResponseDTO();
       productResponseDTO.setId( savedproduct.getId());
       productResponseDTO.setName(savedproduct.getName());
       productResponseDTO.setBrand(savedproduct.getBrand());
       productResponseDTO.setPrice(savedproduct.getPrice());

       CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
       categoryResponseDTO.setId(savedproduct.getCategory().getId());
       categoryResponseDTO.setName(savedproduct.getCategory().getName());

       productResponseDTO.setCategory(categoryResponseDTO);


       return productResponseDTO;

   }

   public Page<ProductResponseDTO> getAllProducts(int page, int size, String sortBy, String direction){

       Pageable pageable = PageRequest.of(
               page,
               size,
               Sort.by(Sort.Direction.fromString(direction), sortBy)
       );
       Page<Product> productPage = productRepository.findAll(pageable);

       return productPage.map(product ->{

           ProductResponseDTO dto = new ProductResponseDTO();
           dto.setId(product.getId());
           dto.setName(product.getName());
           dto.setBrand(product.getBrand());
           dto.setPrice(product.getPrice());

           if(product.getCategory() != null) {
               CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
               categoryDTO.setId(product.getCategory().getId());
               categoryDTO.setName(product.getCategory().getName());

               dto.setCategory(categoryDTO);
           }

           return dto;
       });
   }

   public List<ProductResponseDTO> searchByName(String name){
       List<Product> products = productRepository.findByNameContaining(name);

       return products.stream()
               .map(product -> {

                   ProductResponseDTO dto = new ProductResponseDTO();

                   dto.setId(product.getId());
                   dto.setName(product.getName());
                   dto.setBrand(product.getBrand());
                   dto.setPrice(product.getPrice());

                   return dto;
               })
               .toList();
   }

    public List<ProductResponseDTO> getExpensiveProducts(Double price){

        List<Product> products = productRepository.getExpensiveProducts(price);

        return products.stream()
                .map(product -> {

                    ProductResponseDTO dto = new ProductResponseDTO();

                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setBrand(product.getBrand());
                    dto.setPrice(product.getPrice());

                    return dto;
                })
                .toList();
    }

   public ProductResponseDTO findById(Long id){

       Product product = productRepository
               .findById(id)
               .orElseThrow(() ->
                       new ProductNotFoundException(
                               "Product not found with id " + id
                       ));

       ProductResponseDTO dto = new ProductResponseDTO();

       dto.setId(product.getId());
       dto.setName(product.getName());
       dto.setBrand(product.getBrand());
       dto.setPrice(product.getPrice());

       CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
       categoryDTO.setId(product.getCategory().getId());
       categoryDTO.setName(product.getCategory().getName());

       dto.setCategory(categoryDTO);
       return dto;


   }

   public void deleteById(Long id){

       Product product = productRepository.findById(id)
               .orElseThrow(() ->
                       new ProductNotFoundException("Product not found with id " + id)
               );

       productRepository.delete(product);
   }

   public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO){

       Product existingProduct = productRepository.findById(id)
               .orElseThrow(() ->
                       new ProductNotFoundException(
                               "Product not found with id " + id
                       ));


           existingProduct.setName(productRequestDTO.getName());
           existingProduct.setBrand(productRequestDTO.getBrand());
           existingProduct.setPrice(productRequestDTO.getPrice());
           existingProduct.setQuantity(productRequestDTO.getQuantity());

           Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                   .orElseThrow(() -> new CategoryNotFoundException(
                           "Category not found with id " + productRequestDTO.getCategoryId()
                   ));

           existingProduct.setCategory(category);

           Product updateProject = productRepository.save(existingProduct);

           ProductResponseDTO dto = new ProductResponseDTO();

           dto.setId(updateProject.getId());
           dto.setName(updateProject.getName());
           dto.setBrand(updateProject.getBrand());
           dto.setPrice(updateProject.getPrice());

           CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
           categoryDTO.setId(updateProject.getCategory().getId());
           categoryDTO.setName(updateProject.getCategory().getName());

           dto.setCategory(categoryDTO);

           return dto;

   }

   public Page<ProductResponseDTO> getProductsByCategory(
           Long categoryId,
           int page, int size,
           String sortBy, String direction){

       Category category = categoryRepository.findById(categoryId)
               .orElseThrow(() ->
                       new CategoryNotFoundException("Category not found with id : " + categoryId)
               );

       Sort sort = direction.equalsIgnoreCase("desc")
               ? Sort.by(sortBy).descending()
               : Sort.by(sortBy).ascending();

       Pageable pageable = PageRequest.of(page, size, sort);

       Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

       return productPage.map(product -> {

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
       });
   }

}
