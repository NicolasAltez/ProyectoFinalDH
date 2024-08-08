package com.integrador.servicios_tecnicos.service;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;

import java.util.List;

public interface IProductService {
    ProductDetailsResponseDTO getProductDetailsById(Long productId) throws ResourceNotFoundException;
    List<ProductNameDTO> getProductsRandom();
}
