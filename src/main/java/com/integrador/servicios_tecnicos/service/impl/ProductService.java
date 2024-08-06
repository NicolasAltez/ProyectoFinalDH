package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.models.entity.Product;
import com.integrador.servicios_tecnicos.repository.ProductRepository;
import com.integrador.servicios_tecnicos.service.IProductService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

   @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            return productRepository.save(product);
        }
        throw new ProductNotFoundException("Product not found with id: " + id);
    }

    @Override
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}
