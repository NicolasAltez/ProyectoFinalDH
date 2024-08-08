package com.integrador.servicios_tecnicos.models.dtos.error;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorMessageDTO {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}
