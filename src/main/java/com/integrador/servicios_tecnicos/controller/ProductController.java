package main.java.com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.models.entity.Product;
import com.integrador.servicios_tecnicos.service.IProductService;
import com.integrador.servicios_tecnicos.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    //1. Método @PostMapping para Crear un Producto
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    //2. Método @PutMapping("/{id}") para Actualizar un Producto por ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    //3. Método @DeleteMapping("/{id}") para Eliminar un Producto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    //4. Método @GetMapping("/{id}") para Obtener un Producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return ResponseEntity.ok(product);
    }

    //5. Método @GetMapping para Obtener una Lista de Productos
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
