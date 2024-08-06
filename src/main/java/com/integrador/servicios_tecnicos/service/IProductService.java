package main.java.com.integrador.servicios_tecnicos.service;

import java.util.List;
import java.util.Optional;

import com.integrador.servicios_tecnicos.models.entity.Product;

public interface IProductService {
    //Dedo crear las dto?
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();
}
