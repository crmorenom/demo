package com.bci.demo.user.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bci.demo.user.dto.LoginResponseDTO;
import com.bci.demo.user.dto.PhoneResponseDTO;
import com.bci.demo.user.dto.UserResponseDTO;
import com.bci.demo.user.entity.Phone;
import com.bci.demo.user.entity.User;

class UserMapperTest {

    private UserMapper userMapper;
    private User testUser;
    private Phone testPhone1;
    private Phone testPhone2;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        
        // Crear usuario de prueba
        UUID userId = UUID.randomUUID();
        UUID phone1Id = UUID.randomUUID();
        UUID phone2Id = UUID.randomUUID();
        
        LocalDateTime now = LocalDateTime.now();
        
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Juan Rodriguez");
        testUser.setEmail("juan@rodriguez.org");
        testUser.setPassword("hashedPassword");
        testUser.setCreated(now);
        testUser.setModified(now);
        testUser.setLastLogin(now);
        testUser.setToken("test-jwt-token");
        testUser.setIsActive(true);
        
        // Crear teléfonos de prueba
        testPhone1 = new Phone();
        testPhone1.setId(phone1Id);
        testPhone1.setNumber("1234567");
        testPhone1.setCitycode("1");
        testPhone1.setCountrycode("57");
        testPhone1.setUser(testUser);
        
        testPhone2 = new Phone();
        testPhone2.setId(phone2Id);
        testPhone2.setNumber("7654321");
        testPhone2.setCitycode("2");
        testPhone2.setCountrycode("57");
        testPhone2.setUser(testUser);
        
        testUser.setPhones(Arrays.asList(testPhone1, testPhone2));
    }

    @Test
    void testToResponseDTO_Success() {
        // When
        UserResponseDTO responseDTO = userMapper.toResponseDTO(testUser);

        // Then
        assertNotNull(responseDTO);
        assertEquals(testUser.getId(), responseDTO.getId());
        assertEquals(testUser.getName(), responseDTO.getName());
        assertEquals(testUser.getEmail(), responseDTO.getEmail());
        assertEquals(testUser.getCreated(), responseDTO.getCreated());
        assertEquals(testUser.getModified(), responseDTO.getModified());
        assertEquals(testUser.getLastLogin(), responseDTO.getLastLogin());
        assertEquals(testUser.getToken(), responseDTO.getToken());
        assertEquals(testUser.getIsActive(), responseDTO.isActive());
        
        // Verificar teléfonos
        assertNotNull(responseDTO.getPhones());
        assertEquals(2, responseDTO.getPhones().size());
        
        PhoneResponseDTO phone1DTO = responseDTO.getPhones().get(0);
        assertEquals(testPhone1.getId(), phone1DTO.getId());
        assertEquals(testPhone1.getNumber(), phone1DTO.getNumber());
        assertEquals(testPhone1.getCitycode(), phone1DTO.getCitycode());
        assertEquals(testPhone1.getCountrycode(), phone1DTO.getCountrycode());
        
        PhoneResponseDTO phone2DTO = responseDTO.getPhones().get(1);
        assertEquals(testPhone2.getId(), phone2DTO.getId());
        assertEquals(testPhone2.getNumber(), phone2DTO.getNumber());
    }

    @Test
    void testToResponseDTO_WithEmptyPhones() {
        // Given
        testUser.setPhones(List.of());

        // When
        UserResponseDTO responseDTO = userMapper.toResponseDTO(testUser);

        // Then
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getPhones());
        assertTrue(responseDTO.getPhones().isEmpty());
    }

    @Test
    void testToLoginResponseDTO_Success() {
        // When
        LoginResponseDTO loginDTO = userMapper.toLoginResponseDTO(testUser);

        // Then
        assertNotNull(loginDTO);
        assertEquals(testUser.getId(), loginDTO.getId());
        assertEquals(testUser.getCreated(), loginDTO.getCreated());
        assertEquals(testUser.getModified(), loginDTO.getModified());
        assertEquals(testUser.getLastLogin(), loginDTO.getLastLogin());
        assertEquals(testUser.getToken(), loginDTO.getToken());
        assertEquals(testUser.getIsActive(), loginDTO.getIsActive());
    }

    @Test
    void testToLoginResponseDTO_WithNullValues() {
        // Given
        testUser.setModified(null);
        testUser.setLastLogin(null);

        // When
        LoginResponseDTO loginDTO = userMapper.toLoginResponseDTO(testUser);

        // Then
        assertNotNull(loginDTO);
        assertNull(loginDTO.getModified());
        assertNull(loginDTO.getLastLogin());
    }

    @Test
    void testToResponseDTO_WithSinglePhone() {
        // Given
        testUser.setPhones(List.of(testPhone1));

        // When
        UserResponseDTO responseDTO = userMapper.toResponseDTO(testUser);

        // Then
        assertNotNull(responseDTO);
        assertEquals(1, responseDTO.getPhones().size());
        assertEquals(testPhone1.getNumber(), responseDTO.getPhones().get(0).getNumber());
    }

    @Test
    void testPhoneMapping_AllFields() {
        // When
        UserResponseDTO responseDTO = userMapper.toResponseDTO(testUser);
        PhoneResponseDTO phoneDTO = responseDTO.getPhones().get(0);

        // Then
        assertNotNull(phoneDTO.getId());
        assertNotNull(phoneDTO.getNumber());
        assertNotNull(phoneDTO.getCitycode());
        assertNotNull(phoneDTO.getCountrycode());
    }
}
