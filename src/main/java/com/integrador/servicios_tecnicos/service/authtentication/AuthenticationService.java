package com.integrador.servicios_tecnicos.service.authtentication;

import com.integrador.servicios_tecnicos.exceptions.*;
import com.integrador.servicios_tecnicos.models.dtos.user.LoginUserDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.RegisterUserDTO;
import com.integrador.servicios_tecnicos.models.entity.Role;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.repository.UserRepository;
import com.integrador.servicios_tecnicos.service.email.EmailService;
import com.integrador.servicios_tecnicos.service.impl.UserService;
import com.integrador.servicios_tecnicos.service.verify.VerificationService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationService verificationService;
    private final UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, VerificationService verificationService,UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.userService = userService;
    }

    public User signUp(RegisterUserDTO register) throws MessagingException, SavedUserException {
        User user = createUserToSignUp(register);
        emailService.sendVerificationEmail(user);
        return userService.saveUser(user);
    }

    public User authenticate(LoginUserDTO login) throws AccountNotVerifiedException, ResourceNotFoundException {
        User user = userService.getUserByEmail(login.getEmail());
        validateIfUserIsEnabled(user);
        authenticationManager.authenticate(createAuthenticationToken(login, user));
        return user;
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginUserDTO login, User user) {
        return new UsernamePasswordAuthenticationToken(
                login.getEmail(),
                login.getPassword(),
                getAuthorities(user)
        );
    }

    private void validateIfUserIsEnabled(User user) throws AccountNotVerifiedException {
        if (!user.isEnabled()) {
            throw new AccountNotVerifiedException("Account not verified. Please verify your account");
        }
    }

    private User createUserToSignUp(RegisterUserDTO register){
        return User.builder()
                .username(register.getUsername())
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .verificationCode(verificationService.generateVerificationCode())
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                .enabled(true)
                .build();
    }

}
