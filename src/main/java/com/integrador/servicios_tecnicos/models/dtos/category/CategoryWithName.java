package com.integrador.servicios_tecnicos.models.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryWithName {
    private Long id;
    private String name;
    private String urlImage;
}
