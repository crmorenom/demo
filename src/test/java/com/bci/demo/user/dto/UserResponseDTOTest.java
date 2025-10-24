package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        UserResponseDTO dto = new UserResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getCreated());
        assertNull(dto.getModified());
        assertNull(dto.getLastLogin());
        assertNull(dto.getToken());
        assertFalse(dto.isActive());
        assertNull(dto.getPhones());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        List<PhoneResponseDTO> phones = Collections.singletonList(new PhoneResponseDTO());
        UserResponseDTO dto = new UserResponseDTO(id, "Test User", "test@example.com", now, now, now, "token", true, phones);

        assertEquals(id, dto.getId());
        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(now, dto.getCreated());
        assertEquals(now, dto.getModified());
        assertEquals(now, dto.getLastLogin());
        assertEquals("token", dto.getToken());
        assertTrue(dto.isActive());
        assertEquals(phones, dto.getPhones());
    }

    @Test
    void testSettersAndGetters() {
        UserResponseDTO dto = new UserResponseDTO();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        List<PhoneResponseDTO> phones = Collections.singletonList(new PhoneResponseDTO());

        dto.setId(id);
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setCreated(now);
        dto.setModified(now);
        dto.setLastLogin(now);
        dto.setToken("token");
        dto.setActive(true);
        dto.setPhones(phones);

        assertEquals(id, dto.getId());
        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(now, dto.getCreated());
        assertEquals(now, dto.getModified());
        assertEquals(now, dto.getLastLogin());
        assertEquals("token", dto.getToken());
        assertTrue(dto.isActive());
        assertEquals(phones, dto.getPhones());
    }
}
