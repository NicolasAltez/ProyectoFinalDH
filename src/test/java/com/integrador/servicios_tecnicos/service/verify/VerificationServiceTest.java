package com.integrador.servicios_tecnicos.service.verify;

import com.integrador.servicios_tecnicos.exceptions.*;
import com.integrador.servicios_tecnicos.models.dtos.user.VerifyUserDTO;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.service.email.EmailService;
import com.integrador.servicios_tecnicos.service.impl.UserService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class VerificationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VerificationService verificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class verifyUser {
        @Test
        void testVerifyUserSuccess() throws ResourceNotFoundException, InvalidVerificationCodeException, VerificationCodeExpiredException, MessagingException, SavedUserException {

            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");
            User user = User.builder()
                    .email(verifyUserDTO.getEmail())
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                    .enabled(false)
                    .roles(Collections.emptyList())
                    .build();

            when(userService.getUserByEmail(verifyUserDTO.getEmail())).thenReturn(user);


            verificationService.verifyUser(verifyUserDTO);


            assertNull(user.getVerificationCode());
            assertNull(user.getVerificationCodeExpiresAt());
            assertTrue(user.isEnabled());
            verify(userService, times(1)).getUserByEmail(verifyUserDTO.getEmail());
            verify(userService, times(1)).saveUser(user);
        }

        @Test
        void testVerifyUserNotFound() throws ResourceNotFoundException, SavedUserException {

            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");


            when(userService.getUserByEmail(verifyUserDTO.getEmail()))
                    .thenThrow(new ResourceNotFoundException("User not found"));

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                verificationService.verifyUser(verifyUserDTO);
            });

            assertEquals("User not found", exception.getMessage());
            verify(userService, times(1)).getUserByEmail(verifyUserDTO.getEmail());
            verify(userService, times(0)).saveUser(any(User.class));
        }

        @Test
        void testVerifyUserVerificationCodeExpired() throws ResourceNotFoundException, SavedUserException {
            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");
            User user = User.builder()
                    .email(verifyUserDTO.getEmail())
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            when(userService.getUserByEmail(verifyUserDTO.getEmail())).thenReturn(user);


            VerificationCodeExpiredException exception = assertThrows(VerificationCodeExpiredException.class, () -> {
                verificationService.verifyUser(verifyUserDTO);
            });

            assertEquals("Verification code expired. Please sign up again", exception.getMessage());
            verify(userService, times(1)).getUserByEmail(verifyUserDTO.getEmail());
            verify(userService, times(0)).saveUser(any(User.class));
        }

        @Test
        void testVerifyUserInvalidVerificationCode() throws ResourceNotFoundException, SavedUserException {

            VerifyUserDTO verifyUserDTO = new VerifyUserDTO();
            verifyUserDTO.setEmail("email_test@gmail.com");
            verifyUserDTO.setVerificationCode("123456");
            User user = User.builder()
                    .email(verifyUserDTO.getEmail())
                    .verificationCode("1234567")
                    .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                    .enabled(false)
                    .build();

            when(userService.getUserByEmail(verifyUserDTO.getEmail())).thenReturn(user);

            InvalidVerificationCodeException exception = assertThrows(InvalidVerificationCodeException.class, () -> {
                verificationService.verifyUser(verifyUserDTO);
            });

            assertEquals("Invalid verification code", exception.getMessage());
            verify(userService, times(1)).getUserByEmail(verifyUserDTO.getEmail());
            verify(userService, times(0)).saveUser(any(User.class));
        }
    }

    @Nested
    class resendVerificationCode {

        @Test
        void testResendVerificationCodeSuccess() throws MessagingException, ResourceNotFoundException, AccountAlreadyVerifiedException, SavedUserException {
            User user = User.builder()
                    .email("email@gmail.com")
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            when(userService.getUserByEmail(anyString())).thenReturn(user);
            doNothing().when(emailService).sendVerificationEmail(user);


            verificationService.resendVerificationCode(user.getEmail());


            verify(userService, times(1)).getUserByEmail(user.getEmail());
            verify(userService, times(1)).saveUser(user);
            verify(emailService, times(1)).sendVerificationEmail(user);
            assertNotNull(user.getVerificationCode());
            assertTrue(user.getVerificationCodeExpiresAt().isAfter(LocalDateTime.now()));
        }

        @Test
        void testResendVerificationCodeUserNotFound() throws MessagingException, ResourceNotFoundException, SavedUserException {
            User user = User.builder()
                    .email("email@gmail.com")
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            when(userService.getUserByEmail(user.getEmail()))
                    .thenThrow(new ResourceNotFoundException("User not found"));


            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                verificationService.resendVerificationCode(user.getEmail());
            });

            assertEquals("User not found", exception.getMessage());
            verify(userService, times(1)).getUserByEmail(user.getEmail());
            verify(userService, times(0)).saveUser(any(User.class));
            verify(emailService, times(0)).sendVerificationEmail(user);
        }

        @Test
        void testResendVerificationCodeAccountAlreadyVerified() throws MessagingException, ResourceNotFoundException, SavedUserException {
            User user = User.builder()
                    .email("email@gmail.com")
                    .verificationCode("123456")
                    .verificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                    .enabled(false)
                    .build();

            user.setEnabled(true);
            when(userService.getUserByEmail(anyString())).thenReturn(user);


            AccountAlreadyVerifiedException exception = assertThrows(AccountAlreadyVerifiedException.class, () -> {
                verificationService.resendVerificationCode(user.getEmail());
            });

            assertEquals("Account already verified", exception.getMessage());
            verify(userService, times(1)).getUserByEmail(user.getEmail());
            verify(userService, times(0)).saveUser(any(User.class));
            verify(emailService, times(0)).sendVerificationEmail(user);
        }
    }

}