package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PhoneRequestDTOTest {

    @Test
    void testRecordConstructorAndGetters() {
        PhoneRequestDTO dto = new PhoneRequestDTO("1234567", "1", "57");

        assertEquals("1234567", dto.number());
        assertEquals("1", dto.citycode());
        assertEquals("57", dto.countrycode());
    }
}
