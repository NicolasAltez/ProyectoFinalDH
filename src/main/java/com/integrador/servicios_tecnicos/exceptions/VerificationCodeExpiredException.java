package com.integrador.servicios_tecnicos.exceptions;

public class VerificationCodeExpiredException extends Exception{
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}
