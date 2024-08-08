package com.integrador.servicios_tecnicos.models.dtos.category;

import com.integrador.servicios_tecnicos.models.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;
    private List<Product> productList;
}
