package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;
import com.integrador.servicios_tecnicos.models.entity.Category;
import com.integrador.servicios_tecnicos.models.entity.Product;
import com.integrador.servicios_tecnicos.repository.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    @Nested
    @Tag("random")
    class GetRandomProducts {

        @Test
        void shouldReturnUpTo10RandomProducts() {
            List<Product> mockProducts = generateMockProducts(10);
            when(productRepository.findRandomProducts()).thenReturn(mockProducts);

            List<ProductNameDTO> randomProducts = productService.getProductsRandom();

            assertEquals(10, randomProducts.size());
            verify(productRepository, times(1)).findRandomProducts();
        }

        @Test
        void shouldReturnDifferentRandomProductsEachCall() {
            List<Product> firstBatch = generateMockProducts(10);
            List<Product> secondBatch = generateMockProducts(5);
            when(productRepository.findRandomProducts())
                    .thenReturn(firstBatch)
                    .thenReturn(secondBatch);

            List<ProductNameDTO> firstCall = productService.getProductsRandom();
            List<ProductNameDTO> secondCall = productService.getProductsRandom();

            assertNotEquals(firstCall, secondCall);
            verify(productRepository, times(2)).findRandomProducts();
        }

        @Test
        void shouldReturnEmptyListWhenNoProductsFound() {
            when(productRepository.findRandomProducts()).thenReturn(Collections.emptyList());

            List<ProductNameDTO> randomProducts = productService.getProductsRandom();

            assertTrue(randomProducts.isEmpty());
            verify(productRepository, times(1)).findRandomProducts();
        }

        private List<Product> generateMockProducts(int count) {
            return IntStream.range(0, count)
                    .mapToObj(i -> Product.builder()
                            .id((long) i)
                            .name("Product " + i)
                            .price(BigDecimal.valueOf(100 + i))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .description("Description " + i)
                            .category(Category.builder().id(1L).name("Category").build())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @Tag("details")
    class GetProductDetailsById {

        @Test
        void shouldReturnProductDetailsWhenProductFound() throws ResourceNotFoundException {
            Product mockProduct = createMockProduct();
            ProductDetailsResponseDTO mockProductDTO = createMockProductDTO();
            when(productRepository.findByIdWithCategory(1L)).thenReturn(Optional.of(mockProduct));
            when(modelMapper.map(mockProduct, ProductDetailsResponseDTO.class)).thenReturn(mockProductDTO);

            ProductDetailsResponseDTO result = productService.getProductDetailsById(1L);

            assertEquals(mockProductDTO, result);
            verify(productRepository, times(1)).findByIdWithCategory(1L);
            verify(modelMapper, times(1)).map(mockProduct, ProductDetailsResponseDTO.class);
        }

        @Test
        void shouldThrowExceptionWhenProductNotFound() {
            when(productRepository.findByIdWithCategory(anyLong())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productService.getProductDetailsById(1L));
            verify(productRepository, times(1)).findByIdWithCategory(1L);
        }

        private Product createMockProduct() {
            return Product.builder()
                    .id(1L)
                    .name("Product Name")
                    .price(new BigDecimal("100.00"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .description("Product Description")
                    .category(Category.builder().id(1L).name("Category Name").build())
                    .build();
        }

        private ProductDetailsResponseDTO createMockProductDTO() {
            return ProductDetailsResponseDTO.builder()
                    .id(1L)
                    .name("Product Name")
                    .price(new BigDecimal("100.00"))
                    .description("Product Description")
                    .categoryName("Category Name")
                    .build();
        }
    }
}
