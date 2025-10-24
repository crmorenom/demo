package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        LoginResponseDTO dto = new LoginResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getCreated());
        assertNull(dto.getModified());
        assertNull(dto.getLastLogin());
        assertNull(dto.getToken());
        assertNull(dto.getIsActive());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LoginResponseDTO dto = new LoginResponseDTO(id, now, now, now, "token", true);

        assertEquals(id, dto.getId());
        assertEquals(now, dto.getCreated());
        assertEquals(now, dto.getModified());
        assertEquals(now, dto.getLastLogin());
        assertEquals("token", dto.getToken());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        LoginResponseDTO dto = new LoginResponseDTO();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(id);
        dto.setCreated(now);
        dto.setModified(now);
        dto.setLastLogin(now);
        dto.setToken("token");
        dto.setIsActive(true);

        assertEquals(id, dto.getId());
        assertEquals(now, dto.getCreated());
        assertEquals(now, dto.getModified());
        assertEquals(now, dto.getLastLogin());
        assertEquals("token", dto.getToken());
        assertTrue(dto.getIsActive());
    }
}
