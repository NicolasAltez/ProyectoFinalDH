package com.integrador.servicios_tecnicos.models.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private long expiresIn;

    public LoginResponseDTO(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
