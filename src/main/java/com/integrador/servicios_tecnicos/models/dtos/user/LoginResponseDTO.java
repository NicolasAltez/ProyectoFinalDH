package com.integrador.servicios_tecnicos.models.dtos.user;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private long expiresIn;
    private String username;
    private String email;
    private List<String> roles;
}
