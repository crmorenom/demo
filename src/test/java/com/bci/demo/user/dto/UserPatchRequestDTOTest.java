package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserPatchRequestDTOTest {

    @Test
    void testAllArgsConstructor() {
        List<PhonePatchRequestDTO> phones = Collections.singletonList(new PhonePatchRequestDTO(null, "123", "1", "57"));
        UserPatchRequestDTO dto = new UserPatchRequestDTO("Test User", "test@example.com", "password", phones);

        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals(phones, dto.getPhones());
    }

    @Test
    void testSettersAndGetters() {
        UserPatchRequestDTO dto = new UserPatchRequestDTO(null, null, null, null);
        List<PhonePatchRequestDTO> phones = Collections.singletonList(new PhonePatchRequestDTO(null, "123", "1", "57"));

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
