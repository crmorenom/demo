package com.bci.demo.handlers.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailAlreadyRegisteredExceptionTest {

    @Test
    void testEmailAlreadyRegisteredException() {
        String errorMessage = "Email already registered";
        EmailAlreadyRegisteredException exception = new EmailAlreadyRegisteredException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
