package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.service.impl.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser(){
        return ResponseEntity.ok((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @GetMapping
    public ResponseEntity<List<User>> allUsers(){
        return ResponseEntity.ok(userService.allUsers());
    }

}
