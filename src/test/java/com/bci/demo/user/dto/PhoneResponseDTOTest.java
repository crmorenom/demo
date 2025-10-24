package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class PhoneResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        PhoneResponseDTO dto = new PhoneResponseDTO();
        assertNull(dto.getId());
        assertNull(dto.getNumber());
        assertNull(dto.getCitycode());
        assertNull(dto.getCountrycode());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        PhoneResponseDTO dto = new PhoneResponseDTO(id, "1234567", "1", "57");

        assertEquals(id, dto.getId());
        assertEquals("1234567", dto.getNumber());
        assertEquals("1", dto.getCitycode());
        assertEquals("57", dto.getCountrycode());
    }

    @Test
    void testSettersAndGetters() {
        PhoneResponseDTO dto = new PhoneResponseDTO();
        UUID id = UUID.randomUUID();

        dto.setId(id);
        dto.setNumber("1234567");
        dto.setCitycode("1");
        dto.setCountrycode("57");

        assertEquals(id, dto.getId());
        assertEquals("1234567", dto.getNumber());
        assertEquals("1", dto.getCitycode());
        assertEquals("57", dto.getCountrycode());
    }
}
