package com.integrador.servicios_tecnicos.service.role;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.entity.Role;
import com.integrador.servicios_tecnicos.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRolesByIds(List<Long> roleIds) {
        return roleRepository.findAllById(roleIds);
    }

    public Role getRoleByName(String name) throws ResourceNotFoundException {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }
}
