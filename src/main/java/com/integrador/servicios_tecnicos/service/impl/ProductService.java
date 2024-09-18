package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductDetailsResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductNameDTO;
import com.integrador.servicios_tecnicos.models.dtos.products.ProductRequestDTO;
import com.integrador.servicios_tecnicos.models.entity.Category;
import com.integrador.servicios_tecnicos.models.entity.Characteristic;
import com.integrador.servicios_tecnicos.models.entity.Product;
import com.integrador.servicios_tecnicos.repository.CharacteristicsRepository;
import com.integrador.servicios_tecnicos.repository.ProductRepository;
import com.integrador.servicios_tecnicos.service.ICategoryService;
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
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ICategoryService categoryService;

    private  final CharacteristicsRepository characteristicsRepository;

    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, ICategoryService categoryService, CharacteristicsRepository characteristicsRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.characteristicsRepository = characteristicsRepository;
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
        Category category = findCategoryById(productRequestDTO);


        // Cargar las características existentes desde la base de datos
        List<Characteristic> managedCharacteristics = productRequestDTO.getCharacteristics().stream()
                .map(characteristic -> characteristicsRepository.findById(characteristic.getId())
                        .orElseThrow(() -> new RuntimeException("Characteristic not found: " + characteristic.getId())))
                .toList();

        return productRepository.save(
                Product.builder()
                        .price(productRequestDTO.getPrice())
                        .characteristics(managedCharacteristics)
                        .name(productRequestDTO.getName())
                        .description(productRequestDTO.getDescription())
                        .urlImage(productRequestDTO.getUrlImage())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .category(category)
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

            Category category = findCategoryById(productRequestDTO);


            // Cargar las características existentes desde la base de datos
            List<Characteristic> managedCharacteristics = productRequestDTO.getCharacteristics().stream()
                    .map(characteristic -> characteristicsRepository.findById(characteristic.getId())
                            .orElseThrow(() -> new RuntimeException("Characteristic not found: " + characteristic.getId())))
                    .toList();

            return productRepository.save(
                    Product.builder()
                            .id(id)
                            .price(productRequestDTO.getPrice())
                            .characteristics(managedCharacteristics)
                            .name(productRequestDTO.getName())
                            .description(productRequestDTO.getDescription())
                            .urlImage(productRequestDTO.getUrlImage())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .category(category)
                            .build()
            );



           // return productRepository.save(createProductToModified(productToModified.get(), productRequestDTO));
        } else {
            throw new ResourceNotFoundException("No se encontró el producto a actualizar con id: " + id);
        }
    }

    private Product createProductToModified(Product productToModified, ProductRequestDTO productRequestDTO) {
        Category category = findCategoryById(productRequestDTO);
        return Product.builder()
                .id(productToModified.getId())
                .price(productRequestDTO.getPrice())
                .category(category)
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .build();
    }

    private Category findCategoryById(ProductRequestDTO productRequestDTO){
        return categoryService.findCategoryById(productRequestDTO.getCategoryId());
    }

    public Product getProductById(Long id) throws ResourceNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
