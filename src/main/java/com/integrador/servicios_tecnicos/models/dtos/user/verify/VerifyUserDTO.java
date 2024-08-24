package com.integrador.servicios_tecnicos.models.dtos.user.verify;

import lombok.*;

@Getter
@Setter
public class VerifyUserDTO {
    private String email;
    private String verificationCode;

}
