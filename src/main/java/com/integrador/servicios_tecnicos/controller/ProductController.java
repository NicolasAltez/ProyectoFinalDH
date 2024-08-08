package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;
import com.integrador.servicios_tecnicos.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/random")
    public ResponseEntity<List<ProductNameDTO>> getRandomProduct(){
        return new ResponseEntity<>(productService.getProductsRandom(), HttpStatus.OK);
    }

    @GetMapping("details/{id}")
    public ResponseEntity<ProductDetailsResponseDTO> getProductDetailsById(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.getProductDetailsById(id), HttpStatus.OK);
    }
}
