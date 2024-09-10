package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductRequestDTO;
import com.integrador.servicios_tecnicos.models.entity.Product;
import com.integrador.servicios_tecnicos.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/new")
    public ResponseEntity<Product> createNewProduct(@RequestBody ProductRequestDTO productRequestDTO){
        return new ResponseEntity<>(productService.createNewProduct(productRequestDTO), HttpStatus.CREATED);
    }


    @GetMapping("/all")
    /*public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }*/


    public ResponseEntity<List<ProductDetailsResponseDTO>> getAllProductsWithChar(){
        return new ResponseEntity<>(productService.getAllProductsWithChar(), HttpStatus.OK);
    }

    @GetMapping("/edit")
    public ResponseEntity<Product> editProduct(@RequestBody ProductRequestDTO productRequestDTO, Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.editProduct(productRequestDTO, id), HttpStatus.OK);
    }


}
