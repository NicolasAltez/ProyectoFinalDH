package com.integrador.servicios_tecnicos.models.dtos.products;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDetailsResponseDTO {
    private Long id;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private String urlImage;
}
