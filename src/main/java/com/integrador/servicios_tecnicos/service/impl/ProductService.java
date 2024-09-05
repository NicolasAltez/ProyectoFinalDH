package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductRequestDTO;
import com.integrador.servicios_tecnicos.models.entity.Category;
import com.integrador.servicios_tecnicos.models.entity.Characteristic;
import com.integrador.servicios_tecnicos.models.entity.Product;
import com.integrador.servicios_tecnicos.repository.ProductRepository;
import com.integrador.servicios_tecnicos.service.IProductService;
import jakarta.validation.constraints.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDetailsResponseDTO getProductDetailsById(Long productId) throws ResourceNotFoundException {
        return productRepository.findByIdWithCategory(productId).
                map(product -> modelMapper.map(product, ProductDetailsResponseDTO.class))
                .orElseThrow(() -> {
                    LOGGER.error("Product with id {} not found", productId);
                    return new ResourceNotFoundException("Product with id " + productId + " not found");
                });
    }

    @Override
    public List<ProductNameDTO> getProductsRandom() {
        return productRepository.findRandomProducts()
                .stream()
                .map(product -> modelMapper.map(product, ProductNameDTO.class))
                .toList();
    }

    @Override
    public Product createNewProduct(ProductRequestDTO productRequestDTO) {
        return productRepository.save(
                Product.builder()
                        .price(productRequestDTO.getPrice())
                        .characteristics(productRequestDTO.getCharacteristics())
                        .name(productRequestDTO.getName())
                        .description(productRequestDTO.getDescription())
                        .createdAt(LocalDateTime.now())
                        .category(productRequestDTO.getCategory())
                        .build()
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product editProduct(ProductRequestDTO productRequestDTO, Long id) throws ResourceNotFoundException {
        Optional<Product> productToModified = productRepository.findById(id);
        if (productToModified.isPresent()) {
            return productRepository.save(createProductToModified(productToModified.get(), productRequestDTO));
        } else {
            throw new ResourceNotFoundException("No se encontró el producto a actualizar con id: " + id);
        }
    }

    private Product createProductToModified(Product productToModified, ProductRequestDTO productRequestDTO) {
        return Product.builder()
                .id(productToModified.getId())
                .price(productRequestDTO.getPrice())
                .category(productRequestDTO.getCategory())
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .build();
    }
}
