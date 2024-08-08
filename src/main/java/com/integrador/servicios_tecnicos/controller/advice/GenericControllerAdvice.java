package com.integrador.servicios_tecnicos.controller.advice;
import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.error.ErrorMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GenericControllerAdvice extends ResponseEntityExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GenericControllerAdvice.class);
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> responseBody = Map.of(
                "message", "Validation failed",
                "errors", errors,
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST

        );
        LOGGER.error("error trying validate request body: ", ex);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        String messageException = resourceNotFoundException.getMessage();

        ErrorMessageDTO errorMessage = ErrorMessageDTO.builder()
                .message(messageException)
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .description(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();

        LOGGER.error("resourceNotFoundException: {}", messageException);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
