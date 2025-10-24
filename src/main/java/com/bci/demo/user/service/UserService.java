package com.bci.demo.user.service;

import com.bci.demo.user.dto.LoginRequestDTO;
import com.bci.demo.user.dto.LoginResponseDTO;
import com.bci.demo.user.dto.UserPatchRequestDTO;
import com.bci.demo.user.dto.UserRequestDTO;
import com.bci.demo.user.dto.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(UUID id);
    UserResponseDTO deleteUser(UUID id);
    UserResponseDTO patchUser(UUID id, UserPatchRequestDTO userPatchDTO);
    
}
