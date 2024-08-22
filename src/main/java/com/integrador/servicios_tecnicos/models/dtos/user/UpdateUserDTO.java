package com.integrador.servicios_tecnicos.models.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private long id;
    private String username;
    private String email;
    private String password;
    private List<Long> roleIds;
    private List<Long> privilegeIds;
}
