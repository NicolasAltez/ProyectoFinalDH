package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.exceptions.AccountNotVerifiedException;
import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.exceptions.SavedUserException;
import com.integrador.servicios_tecnicos.models.dtos.user.*;
import com.integrador.servicios_tecnicos.models.dtos.user.login.LoginResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.login.LoginUserDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.register.RegisterUserDTO;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.service.authtentication.AuthenticationService;
import com.integrador.servicios_tecnicos.service.jwt.JwtService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterUserDTO registerUserDTO) throws MessagingException, SavedUserException, ResourceNotFoundException {

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signUp(registerUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDTO) throws AccountNotVerifiedException, ResourceNotFoundException {
        User authenticatedUser = authenticationService.authenticate(loginUserDTO);
        return ResponseEntity.ok(LoginResponseDTO.fromEntity(authenticatedUser, jwtService.generateToken(authenticatedUser), jwtService.getExpirationTime()));
    }
}
