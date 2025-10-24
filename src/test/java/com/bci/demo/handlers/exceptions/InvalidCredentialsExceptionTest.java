package com.bci.demo.handlers.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidCredentialsExceptionTest {

    @Test
    void testInvalidCredentialsException() {
        String errorMessage = "Invalid credentials";
        InvalidCredentialsException exception = new InvalidCredentialsException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
