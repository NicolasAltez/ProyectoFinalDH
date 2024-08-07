package com.integrador.servicios_tecnicos.models.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {
    private String email;
    private String password;
}
