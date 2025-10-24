package com.bci.demo.handlers.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidEmailFormatExceptionTest {

    @Test
    void testInvalidEmailFormatException() {
        String errorMessage = "Invalid email format";
        InvalidEmailFormatException exception = new InvalidEmailFormatException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
