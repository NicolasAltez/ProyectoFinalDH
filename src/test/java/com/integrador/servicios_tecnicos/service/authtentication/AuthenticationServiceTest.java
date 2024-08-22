package com.integrador.servicios_tecnicos.service.authtentication;

import com.integrador.servicios_tecnicos.exceptions.*;
import com.integrador.servicios_tecnicos.models.dtos.user.LoginResponseDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.LoginUserDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.RegisterUserDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.UserResponseDTO;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.service.email.EmailService;
import com.integrador.servicios_tecnicos.service.user.UserService;
import com.integrador.servicios_tecnicos.service.verify.VerificationService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterUserDTO registerUserDTO;

    private LoginUserDTO loginUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerUserDTO = new RegisterUserDTO();
        loginUserDTO = new LoginUserDTO();
        registerUserDTO.setEmail("email@test.com");
        registerUserDTO.setUsername("username_test");
        registerUserDTO.setPassword("password_test");

        loginUserDTO.setEmail("email_test@gmail.com");
        loginUserDTO.setPassword("password_test");
    }

    @Nested
    class signUp {
        @Test
        void testSignUpSuccess() throws MessagingException, SavedUserException {

            User savedUser = User.builder()
                    .username(registerUserDTO.getUsername())
                    .email(registerUserDTO.getEmail())
                    .password(registerUserDTO.getPassword())
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                    .enabled(true)
                    .roles(Collections.emptyList())
                    .build();

            when(passwordEncoder.encode(registerUserDTO.getPassword())).thenReturn("password_test");
            when(userService.saveUser(savedUser)).thenReturn(savedUser);
            when(verificationService.generateVerificationCode()).thenReturn("123456");
            doNothing().when(emailService).sendVerificationEmail(savedUser);

            UserResponseDTO result = authenticationService.signUp(registerUserDTO);

            assertEquals(registerUserDTO.getUsername(), result.getUsername());
            assertEquals(registerUserDTO.getEmail(), result.getEmail());
            verify(userService, times(1)).saveUser(savedUser);
            verify(emailService, times(1)).sendVerificationEmail(savedUser);
        }
    }

    @Nested
    class authenticate {
        @Test
        void testAuthenticateSuccess() throws ResourceNotFoundException, AccountNotVerifiedException {

            User user = User.builder()
                    .email(loginUserDTO.getEmail())
                    .password("password_test")
                    .enabled(true)
                    .roles(Collections.emptyList())
                    .build();

            when(userService.getUserByEmail(anyString())).thenReturn(user);


            LoginResponseDTO result = authenticationService.authenticate(loginUserDTO);


            assertEquals(loginUserDTO.getEmail(), result.getEmail());
            verify(userService, times(1)).getUserByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        void testAuthenticateUserNotFound() throws ResourceNotFoundException {

            LoginUserDTO loginUserDTO = new LoginUserDTO();
            loginUserDTO.setEmail("test@example.com");

            when(userService.getUserByEmail(loginUserDTO.getEmail()))
                    .thenThrow(new ResourceNotFoundException("User not found"));

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                authenticationService.authenticate(loginUserDTO);
            });

            assertEquals("User not found", exception.getMessage());

            verify(userService, times(1)).getUserByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }


        @Test
        void testAuthenticateAccountNotVerified() throws ResourceNotFoundException {
            User user = User.builder()
                    .email(loginUserDTO.getEmail())
                    .password("encodedPassword")
                    .enabled(false)
                    .build();

            when(userService.getUserByEmail(loginUserDTO.getEmail())).thenReturn(user);

            AccountNotVerifiedException exception = assertThrows(AccountNotVerifiedException.class, () -> {
                authenticationService.authenticate(loginUserDTO);
            });

            assertEquals("Account not verified. Please verify your account", exception.getMessage());
            verify(userService, times(1)).getUserByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        void testAuthenticateBadCredentials() throws ResourceNotFoundException {

            User user = User.builder()
                    .email(loginUserDTO.getEmail())
                    .password("encodedPassword")
                    .enabled(true)
                    .roles(Collections.emptyList())
                    .build();

            when(userService.getUserByEmail(loginUserDTO.getEmail())).thenReturn(user);
            doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

            assertThrows(BadCredentialsException.class, () -> {
                authenticationService.authenticate(loginUserDTO);
            });

            verify(userService, times(1)).getUserByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }
    }
}