package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.models.dtos.user.verify.VerifyUserDTO;
import com.integrador.servicios_tecnicos.service.verify.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO) {
        try {
            verificationService.verifyUser(verifyUserDTO);
            return ResponseEntity.ok("User verified successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendVerification(@RequestParam String email) {
        try {
            verificationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
