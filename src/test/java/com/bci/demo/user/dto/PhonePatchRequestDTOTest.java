package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class PhonePatchRequestDTOTest {

    @Test
    void testRecordConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        PhonePatchRequestDTO dto = new PhonePatchRequestDTO(id, "1234567", "1", "57");

        assertEquals(id, dto.id());
        assertEquals("1234567", dto.number());
        assertEquals("1", dto.citycode());
        assertEquals("57", dto.countrycode());
    }
}
