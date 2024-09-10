package com.integrador.servicios_tecnicos.models.dtos.products;


import com.integrador.servicios_tecnicos.models.dtos.characteristics.CharacteristicDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
    private List<CharacteristicDTO> characteristics;
}
