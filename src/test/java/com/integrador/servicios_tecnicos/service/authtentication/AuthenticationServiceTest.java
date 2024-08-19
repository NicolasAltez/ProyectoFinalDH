package com.integrador.servicios_tecnicos.service.authtentication;

import com.integrador.servicios_tecnicos.exceptions.*;
import com.integrador.servicios_tecnicos.models.dtos.user.LoginUserDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.RegisterUserDTO;
import com.integrador.servicios_tecnicos.models.dtos.user.VerifyUserDTO;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.repository.UserRepository;
import com.integrador.servicios_tecnicos.service.email.EmailService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

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
        void testSignUpSuccess() throws MessagingException {

            User savedUser = User.builder()
                    .username(registerUserDTO.getUsername())
                    .email(registerUserDTO.getEmail())
                    .password(registerUserDTO.getPassword())
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                    .enabled(true)
                    .build();

            when(passwordEncoder.encode(registerUserDTO.getPassword())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            doNothing().when(emailService).sendVerificationEmail(anyString(), anyString(), anyString());

            User result = authenticationService.signUp(registerUserDTO);


            assertEquals(registerUserDTO.getUsername(), result.getUsername());
            assertEquals(registerUserDTO.getEmail(), result.getEmail());
            verify(userRepository, times(1)).save(any(User.class));
            verify(emailService, times(1)).sendVerificationEmail(anyString(), anyString(), anyString());
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
                    .build();

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));


            User result = authenticationService.authenticate(loginUserDTO);


            assertEquals(loginUserDTO.getEmail(), result.getEmail());
            verify(userRepository, times(1)).findByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        void testAuthenticateUserNotFound() {

            LoginUserDTO loginUserDTO = new LoginUserDTO();

            when(userRepository.findByEmail(loginUserDTO.getEmail())).thenReturn(Optional.empty());


            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                authenticationService.authenticate(loginUserDTO);
            });

            assertEquals("User not found", exception.getMessage());
            verify(userRepository, times(1)).findByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        void testAuthenticateAccountNotVerified() {
            User user = User.builder()
                    .email(loginUserDTO.getEmail())
                    .password("encodedPassword")
                    .enabled(false)
                    .build();

            when(userRepository.findByEmail(loginUserDTO.getEmail())).thenReturn(Optional.of(user));

            AccountNotVerifiedException exception = assertThrows(AccountNotVerifiedException.class, () -> {
                authenticationService.authenticate(loginUserDTO);
            });

            assertEquals("Account not verified. Please verify your account", exception.getMessage());
            verify(userRepository, times(1)).findByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        void testAuthenticateBadCredentials() {

            User user = User.builder()
                    .email(loginUserDTO.getEmail())
                    .password("encodedPassword")
                    .enabled(true)
                    .build();

            when(userRepository.findByEmail(loginUserDTO.getEmail())).thenReturn(Optional.of(user));
            doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

            assertThrows(BadCredentialsException.class, () -> {
                authenticationService.authenticate(loginUserDTO);
            });

            verify(userRepository, times(1)).findByEmail(loginUserDTO.getEmail());
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }
    }

    @Nested
    class verifyUser {
        @Test
        void testVerifyUserSuccess() throws ResourceNotFoundException, InvalidVerificationCodeException, VerificationCodeExpiredException {

            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");
            User user = User.builder()
                    .email(verifyUserDTO.getEmail())
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                    .enabled(false)
                    .build();

            when(userRepository.findByEmail(verifyUserDTO.getEmail())).thenReturn(Optional.of(user));


            authenticationService.verifyUser(verifyUserDTO);


            assertNull(user.getVerificationCode());
            assertNull(user.getVerificationCodeExpiresAt());
            assertTrue(user.isEnabled());
            verify(userRepository, times(1)).findByEmail(verifyUserDTO.getEmail());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        void testVerifyUserNotFound() {

            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");


            when(userRepository.findByEmail(verifyUserDTO.getEmail())).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                authenticationService.verifyUser(verifyUserDTO);
            });

            assertEquals("User not found", exception.getMessage());
            verify(userRepository, times(1)).findByEmail(verifyUserDTO.getEmail());
            verify(userRepository, times(0)).save(any(User.class));
        }

        @Test
        void testVerifyUserVerificationCodeExpired() {
            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");
            User user = User.builder()
                    .email(verifyUserDTO.getEmail())
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            when(userRepository.findByEmail(verifyUserDTO.getEmail())).thenReturn(Optional.of(user));


            VerificationCodeExpiredException exception = assertThrows(VerificationCodeExpiredException.class, () -> {
                authenticationService.verifyUser(verifyUserDTO);
            });

            assertEquals("Verification code expired. Please sign up again", exception.getMessage());
            verify(userRepository, times(1)).findByEmail(verifyUserDTO.getEmail());
            verify(userRepository, times(0)).save(any(User.class));
        }

        @Test
        void testVerifyUserInvalidVerificationCode() {

            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");
            User user = User.builder()
                    .email(verifyUserDTO.getEmail())
                    .verificationCode("1234567")
                    .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                    .enabled(false)
                    .build();

            when(userRepository.findByEmail(verifyUserDTO.getEmail())).thenReturn(Optional.of(user));

            InvalidVerificationCodeException exception = assertThrows(InvalidVerificationCodeException.class, () -> {
                authenticationService.verifyUser(verifyUserDTO);
            });

            assertEquals("Invalid verification code", exception.getMessage());
            verify(userRepository, times(1)).findByEmail(verifyUserDTO.getEmail());
            verify(userRepository, times(0)).save(any(User.class));
        }
    }

    @Nested
    class resendVerificationCode {


        @Test
        void testResendVerificationCodeSuccess() throws MessagingException, ResourceNotFoundException, AccountAlreadyVerifiedException {
            User user = User.builder()
                    .email("email@gmail.com")
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            doNothing().when(emailService).sendVerificationEmail(anyString(), anyString(), anyString());


            authenticationService.resendVerificationCode(user.getEmail());


            verify(userRepository, times(1)).findByEmail(user.getEmail());
            verify(userRepository, times(1)).save(user);
            verify(emailService, times(1)).sendVerificationEmail(anyString(), anyString(), anyString());
            assertNotNull(user.getVerificationCode());
            assertTrue(user.getVerificationCodeExpiresAt().isAfter(LocalDateTime.now()));
        }

        @Test
        void testResendVerificationCodeUserNotFound() throws MessagingException {
            User user = User.builder()
                    .email("email@gmail.com")
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());


            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                authenticationService.resendVerificationCode(user.getEmail());
            });

            assertEquals("User not found", exception.getMessage());
            verify(userRepository, times(1)).findByEmail(user.getEmail());
            verify(userRepository, times(0)).save(any(User.class));
            verify(emailService, times(0)).sendVerificationEmail(anyString(), anyString(), anyString());
        }

        @Test
        void testResendVerificationCodeAccountAlreadyVerified() throws MessagingException {
            User user = User.builder()
                    .email("email@gmail.com")
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            user.setEnabled(true);
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));


            AccountAlreadyVerifiedException exception = assertThrows(AccountAlreadyVerifiedException.class, () -> {
                authenticationService.resendVerificationCode(user.getEmail());
            });

            assertEquals("Account already verified", exception.getMessage());
            verify(userRepository, times(1)).findByEmail(user.getEmail());
            verify(userRepository, times(0)).save(any(User.class));
            verify(emailService, times(0)).sendVerificationEmail(anyString(), anyString(), anyString());
        }
    }
}