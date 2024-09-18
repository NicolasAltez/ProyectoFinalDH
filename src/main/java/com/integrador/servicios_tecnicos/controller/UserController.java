package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.user.UserResponseDTO;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/authenticated")
    public ResponseEntity<UserResponseDTO> getAuthenticatedUser(){
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> allUsers(){
        return ResponseEntity.ok(userService.allUsers());
    }

    @PutMapping("/update-roles")
    public ResponseEntity<UserResponseDTO> updateUserRoles(@RequestParam Long userId, @RequestParam List<Long> roleIds) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.updateUserRoles(userId, roleIds));
    }

    @GetMapping("details/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id));
    }


}
