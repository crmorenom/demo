package com.bci.demo.handlers.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidPasswordFormatExceptionTest {

    @Test
    void testInvalidPasswordFormatException() {
        String errorMessage = "Invalid password format";
        InvalidPasswordFormatException exception = new InvalidPasswordFormatException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
