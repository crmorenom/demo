package com.bci.demo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bci.demo.cfg.security.JwtUtil;
import com.bci.demo.user.controller.UserController;
import com.bci.demo.user.dto.PhoneResponseDTO;
import com.bci.demo.user.dto.UserPatchRequestDTO;
import com.bci.demo.user.dto.UserResponseDTO;
import com.bci.demo.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@Import(com.bci.demo.cfg.validation.PasswordConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;


    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        PhoneResponseDTO phoneResponseDTO = new PhoneResponseDTO(UUID.randomUUID(), "1234567", "1", "57");
        userResponseDTO = new UserResponseDTO(userId, "Test User", "test@example.com", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "token", true, Collections.singletonList(phoneResponseDTO));
    }

    @Test
    @WithMockUser
    void getAllUsers_success() throws Exception {
        List<UserResponseDTO> users = Collections.singletonList(userResponseDTO);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void getUserById_success() throws Exception {
        when(userService.getUserById(any(UUID.class))).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/users/{id}", userResponseDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userResponseDTO.getId().toString()));
    }

    @Test
    @WithMockUser
    void deleteUser_success() throws Exception {
        when(userService.deleteUser(any(UUID.class))).thenReturn(userResponseDTO);

        mockMvc.perform(delete("/api/users/{id}", userResponseDTO.getId()).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void patchUser_success() throws Exception {
        UserPatchRequestDTO patchDTO = new UserPatchRequestDTO("Updated Name", null, null, null);

        when(userService.patchUser(any(UUID.class), any(UserPatchRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(patch("/api/users/{id}", userResponseDTO.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test User"));
    }

    @Test
    void login_success() throws Exception {
        com.bci.demo.user.dto.LoginRequestDTO loginRequestDTO = new com.bci.demo.user.dto.LoginRequestDTO("test@example.com", "0Password1");
        com.bci.demo.user.dto.LoginResponseDTO loginResponseDTO = new com.bci.demo.user.dto.LoginResponseDTO();
        when(userService.login(any(com.bci.demo.user.dto.LoginRequestDTO.class))).thenReturn(loginResponseDTO);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void registerUser_success() throws Exception {
        com.bci.demo.user.dto.UserRequestDTO userRequestDTO = new com.bci.demo.user.dto.UserRequestDTO("Test User", "test@example.com", "Password22", Collections.emptyList());
        when(userService.registerUser(any(com.bci.demo.user.dto.UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated());
    }
}