package com.integrador.servicios_tecnicos.service.email;

import com.integrador.servicios_tecnicos.models.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
    }

    @Test
    void testSendVerificationEmailSuccess() throws MessagingException {

        User user = User.builder()
                .email("email_user@gmail.com")
                .verificationCode("123456")
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                .enabled(false)
                .build();
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(emailSender).send(any(MimeMessage.class));

        emailService.sendVerificationEmail(user);

        verify(emailSender, times(1)).createMimeMessage();
        verify(emailSender, times(1)).send(mimeMessage);

    }

    @Test
    void testSendVerificationEmailThrowsMailException() {
        User user = User.builder()
                .email("email_user@gmail.com")
                .verificationCode("123456")
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                .enabled(false)
                .build();
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(RuntimeException.class).when(emailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> {
            emailService.sendVerificationEmail(user);
        });

        verify(emailSender, times(1)).createMimeMessage();
        verify(emailSender, times(1)).send(mimeMessage);
    }
}