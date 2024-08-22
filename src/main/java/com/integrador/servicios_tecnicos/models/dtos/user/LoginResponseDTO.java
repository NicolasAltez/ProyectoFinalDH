package com.integrador.servicios_tecnicos.models.dtos.user;

import com.integrador.servicios_tecnicos.models.entity.Role;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String username;
    private String email;
    private long expiresIn;
    private List<Role> roles;
}
