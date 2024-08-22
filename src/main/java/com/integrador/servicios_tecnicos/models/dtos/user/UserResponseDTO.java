package com.integrador.servicios_tecnicos.models.dtos.user;

import com.integrador.servicios_tecnicos.models.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private LocalDateTime verificationCodeExpiresAt;
    private List<Role> roles;
}
