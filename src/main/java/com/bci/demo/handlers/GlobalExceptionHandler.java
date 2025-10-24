package com.bci.demo.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bci.demo.handlers.exceptions.EmailAlreadyRegisteredException;
import com.bci.demo.handlers.exceptions.InvalidCredentialsException;
import com.bci.demo.handlers.exceptions.InvalidEmailFormatException;
import com.bci.demo.handlers.exceptions.ResourceNotFoundException;
import com.bci.demo.user.dto.ApiResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    private <T> ApiResponseDTO<T> createErrorResponse(int code, String message) {
        return new ApiResponseDTO<>(code, message, null);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponseDTO<Object>> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponseDTO<Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(InvalidEmailFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<Object>> handleInvalidEmailFormat(InvalidEmailFormatException ex) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");
        return new ResponseEntity<>(createErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponseDTO<Object>> handleGeneric(Exception ex) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}