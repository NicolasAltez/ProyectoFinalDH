package com.integrador.servicios_tecnicos.models.dtos.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductNameDTO {
    private Long id;
    private String name;
    private String urlImage;
}
