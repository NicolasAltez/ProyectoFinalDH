package com.integrador.servicios_tecnicos.service;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductRequestDTO;
import com.integrador.servicios_tecnicos.models.entity.Product;

import java.util.List;

public interface IProductService {
    ProductDetailsResponseDTO getProductDetailsById(Long productId) throws ResourceNotFoundException;

    List<ProductNameDTO> getProductsRandom();

    Product createNewProduct(ProductRequestDTO productRequestDTO);

    List<Product> getAllProducts();

    Product editProduct(ProductRequestDTO productRequestDTO,Long id) throws ResourceNotFoundException;
}
