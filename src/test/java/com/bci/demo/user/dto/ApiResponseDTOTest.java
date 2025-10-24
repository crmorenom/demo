package com.bci.demo.user.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        ApiResponseDTO<String> dto = new ApiResponseDTO<>();
        assertEquals(0, dto.getCodigo());
        assertNull(dto.getMensaje());
        assertNull(dto.getData());
    }

    @Test
    void testAllArgsConstructor() {
        String data = "test data";
        ApiResponseDTO<String> dto = new ApiResponseDTO<>(200, "Success", data);
        assertEquals(200, dto.getCodigo());
        assertEquals("Success", dto.getMensaje());
        assertEquals(data, dto.getData());
    }

    @Test
    void testCustomArgsConstructor() {
        ApiResponseDTO<String> dto = new ApiResponseDTO<>(404, "Not Found");
        assertEquals(404, dto.getCodigo());
        assertEquals("Not Found", dto.getMensaje());
        assertNull(dto.getData());
    }

    @Test
    void testSettersAndGetters() {
        ApiResponseDTO<Integer> dto = new ApiResponseDTO<>();
        dto.setCodigo(500);
        dto.setMensaje("Internal Server Error");
        dto.setData(123);

        assertEquals(500, dto.getCodigo());
        assertEquals("Internal Server Error", dto.getMensaje());
        assertEquals(123, dto.getData());
    }
}
