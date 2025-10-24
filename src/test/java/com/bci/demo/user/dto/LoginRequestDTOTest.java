package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        LoginRequestDTO dto = new LoginRequestDTO();
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        LoginRequestDTO dto = new LoginRequestDTO("test@example.com", "password");
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("password");

        assertEquals("test@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
    }
}
