package com.integrador.servicios_tecnicos.models.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDTO {
    private String email;
    private String verificationCode;

}
