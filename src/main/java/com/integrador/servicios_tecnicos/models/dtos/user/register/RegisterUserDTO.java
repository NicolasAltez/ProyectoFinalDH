package com.integrador.servicios_tecnicos.models.dtos.user.register;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    private String email;
    private String password;
    private String username;
}
