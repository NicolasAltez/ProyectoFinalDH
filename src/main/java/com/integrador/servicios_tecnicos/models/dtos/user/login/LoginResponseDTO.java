package com.integrador.servicios_tecnicos.models.dtos.user.login;

import com.integrador.servicios_tecnicos.models.entity.Role;
import com.integrador.servicios_tecnicos.models.entity.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String username;
    private String email;
    private long expiresIn;
    private List<String> roles;

    public static LoginResponseDTO fromEntity(User user, String token, long expiresIn) {
        return LoginResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .expiresIn(expiresIn)
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
