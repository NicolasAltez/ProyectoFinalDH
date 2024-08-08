package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;
import com.integrador.servicios_tecnicos.repository.ProductRepository;
import com.integrador.servicios_tecnicos.service.IProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
