package com.integrador.servicios_tecnicos.models.dtos.products;

import com.integrador.servicios_tecnicos.models.entity.Category;
import com.integrador.servicios_tecnicos.models.entity.Characteristic;
import com.integrador.servicios_tecnicos.models.entity.Reservation;
import jakarta.validation.constraints.*;
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
public class ProductRequestDTO {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Product type is required")
    @Size(max = 100, message = "Product name must be less than 100 characters")
    private String name;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount with up to 8 integer digits and 2 fractional digits")
    private BigDecimal price;

    @NotEmpty(message = "characteristics are required")
    private List<Characteristic> characteristics;


}
