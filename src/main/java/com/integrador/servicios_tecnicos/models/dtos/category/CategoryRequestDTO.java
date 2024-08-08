package com.integrador.servicios_tecnicos.models.dtos.category;

import jakarta.validation.constraints.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryRequestDTO {

    @NotNull(message ="Category type is required")
    @NotBlank(message = "Category type cannot be blank")
    @Size(min = 3, max = 100, message = "Category type must be between 3 and 100 characters")
    private String categoryType;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters")
    private String description;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;


}
