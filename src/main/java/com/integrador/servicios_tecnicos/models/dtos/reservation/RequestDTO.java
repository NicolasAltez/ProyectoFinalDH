package com.integrador.servicios_tecnicos.models.dtos.reservation;

import com.integrador.servicios_tecnicos.models.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestDTO {
    private String email;
    private Long productId;
    private LocalDateTime reservationDate;
    private String address;
}
