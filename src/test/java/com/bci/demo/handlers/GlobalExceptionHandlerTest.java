package com.bci.demo.handlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.bci.demo.handlers.exceptions.EmailAlreadyRegisteredException;
import com.bci.demo.handlers.exceptions.InvalidCredentialsException;
import com.bci.demo.handlers.exceptions.InvalidEmailFormatException;
import com.bci.demo.handlers.exceptions.ResourceNotFoundException;
import com.bci.demo.user.dto.ApiResponseDTO;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleEmailAlreadyRegistered() {
        String errorMessage = "El correo ya está registrado";
        EmailAlreadyRegisteredException exception = new EmailAlreadyRegisteredException(errorMessage);
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleEmailAlreadyRegistered(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMensaje());
        assertNull(response.getBody().getData());
    }


    @Test
    void testHandleInvalidCredentials() {
        String errorMessage = "Credenciales inválidas";
        InvalidCredentialsException exception = new InvalidCredentialsException(errorMessage);
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleInvalidCredentials(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMensaje());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleInvalidEmailFormat() {
        String errorMessage = "Formato de email inválido";
        InvalidEmailFormatException exception = new InvalidEmailFormatException(errorMessage);
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleInvalidEmailFormat(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMensaje());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleResourceNotFound() {
        String errorMessage = "Usuario no encontrado";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleResourceNotFound(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMensaje());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("userRequestDTO", "email", "Email inválido");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleValidation(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email inválido", response.getBody().getMensaje());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleMethodArgumentNotValidException_NoFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleValidation(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de validación", response.getBody().getMensaje());
    }

    @Test
    void testHandleGenericException() {
        String errorMessage = "Error inesperado";
        Exception exception = new Exception(errorMessage);
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleGeneric(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMensaje().contains(errorMessage));
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleInvalidPasswordFormat() {
        String errorMessage = "Formato de contraseña inválido";
        ResponseEntity<ApiResponseDTO<Object>> response = globalExceptionHandler.handleInvalidEmailFormat(
            new InvalidEmailFormatException(errorMessage)
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}