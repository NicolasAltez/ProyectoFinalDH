package com.integrador.servicios_tecnicos.service.verify;

import com.integrador.servicios_tecnicos.exceptions.*;
import com.integrador.servicios_tecnicos.models.dtos.user.VerifyUserDTO;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.service.email.EmailService;
import com.integrador.servicios_tecnicos.service.impl.UserService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationService {

    private final EmailService emailService;
    private final UserService userService;

    public VerificationService(EmailService emailService,UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    private void verifyVerificationCode(User user, String verificationCode) throws InvalidVerificationCodeException, SavedUserException {
        if (user.getVerificationCode().equals(verificationCode)) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userService.saveUser(user);
        } else {
            throw new InvalidVerificationCodeException("Invalid verification code");
        }
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public void resendVerificationCode(String email) throws MessagingException, ResourceNotFoundException, AccountAlreadyVerifiedException, SavedUserException {
        User user = userService.getUserByEmail(email);
        validateIfUserIsEnabled(user);

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));

        emailService.sendVerificationEmail(user);
        userService.saveUser(user);
    }

    public void verifyUser(VerifyUserDTO verify) throws InvalidVerificationCodeException, VerificationCodeExpiredException, ResourceNotFoundException, MessagingException, SavedUserException {
        User user = userService.getUserByEmail(verify.getEmail());
        validateVerificationCodeExpiration(user);
        verifyVerificationCode(user, verify.getVerificationCode());
        emailService.sendRegistrationSuccessEmail(user.getEmail());
    }

    private void validateIfUserIsEnabled(User user) throws AccountAlreadyVerifiedException {
        if (user.isEnabled()) {
            throw new AccountAlreadyVerifiedException("Account already verified");
        }
    }

    private void validateVerificationCodeExpiration(User user) throws VerificationCodeExpiredException {
        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code expired. Please sign up again");
        }
    }

}
