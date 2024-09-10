package com.integrador.servicios_tecnicos.models.dtos.characteristics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CharacteristicDTO {
    private String name;
    private String description;
}
