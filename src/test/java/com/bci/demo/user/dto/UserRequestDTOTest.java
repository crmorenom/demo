package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserRequestDTOTest {

    @Test
    void testAllArgsConstructor() {
        List<PhoneRequestDTO> phones = Collections.singletonList(new PhoneRequestDTO("1234567", "1", "57"));
        UserRequestDTO dto = new UserRequestDTO("Test User", "test@example.com", "password", phones);

        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals(phones, dto.getPhones());
    }

    @Test
    void testSettersAndGetters() {
        UserRequestDTO dto = new UserRequestDTO(null, null, null, null);
        List<PhoneRequestDTO> phones = Collections.singletonList(new PhoneRequestDTO("1234567", "1", "57"));

        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setPhones(phones);

        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals(phones, dto.getPhones());
    }
}
