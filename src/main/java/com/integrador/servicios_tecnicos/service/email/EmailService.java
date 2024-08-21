package com.integrador.servicios_tecnicos.service.email;

import com.integrador.servicios_tecnicos.models.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendRegistrationSuccessEmail(String to) throws MessagingException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Registration Successful");
        helper.setText("<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">We are excited to inform you that your registration was successful!</p>"
                + "<p style=\"font-size: 16px;\">You can now log in and start using our services.</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Your registered email:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + to + "</p>"
                + "</div>"
                + "<p style=\"font-size: 16px;\">Thank you for joining us, and we look forward to serving you!</p>"
                + "</div>"
                + "</body>"
                + "</html>", true);
        emailSender.send(message);
    }

    public void sendVerificationEmail(User user) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Account verification");
        helper.setText("<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + user.getVerificationCode() + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>",true);
        emailSender.send(message);
    }
}
