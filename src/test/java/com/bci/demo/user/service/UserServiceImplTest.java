package com.bci.demo.user.service;

import com.bci.demo.cfg.security.JwtUtil;
import com.bci.demo.handlers.exceptions.EmailAlreadyRegisteredException;
import com.bci.demo.handlers.exceptions.InvalidCredentialsException;
import com.bci.demo.handlers.exceptions.InvalidEmailFormatException;
import com.bci.demo.handlers.exceptions.ResourceNotFoundException;
import com.bci.demo.user.dto.*;
import com.bci.demo.user.entity.Phone;
import com.bci.demo.user.entity.User;
import com.bci.demo.user.mapper.UserMapper;
import com.bci.demo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);
        user.setPhones(new ArrayList<>());

        PhoneRequestDTO phoneRequestDTO = new PhoneRequestDTO("1234567", "1", "57");
        userRequestDTO = new UserRequestDTO("Test User", "test@example.com", "Password123!", Collections.singletonList(phoneRequestDTO));

        PhoneResponseDTO phoneResponseDTO = new PhoneResponseDTO(UUID.randomUUID(),"1234567", "1", "57");
        userResponseDTO = new UserResponseDTO(userId, "Test User", "test@example.com", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "token", true, Collections.singletonList(phoneResponseDTO));
    }

        @Test
        void login_success() {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test@example.com", "password");
            UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("test@example.com").password("encodedPassword").authorities(new ArrayList<>()).build();

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
            when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("test_token");

            LoginResponseDTO response = userService.login(loginRequestDTO);

            assertNotNull(response);
            assertEquals("test_token", response.getToken());
        }

        @Test
        void login_userNotFound() {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test@example.com", "password");
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            assertThrows(InvalidCredentialsException.class, () -> userService.login(loginRequestDTO));
        }

        @Test
        void login_invalidPassword() {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test@example.com", "password");
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
            assertThrows(InvalidCredentialsException.class, () -> userService.login(loginRequestDTO));
        }

        @Test
        void registerUser_success() {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build());
            when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("test_token");

            UserResponseDTO response = userService.registerUser(userRequestDTO);

            assertNotNull(response);
            assertEquals("test@example.com", response.getEmail());
        }

        @Test
        void registerUser_emailAlreadyRegistered() {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            assertThrows(EmailAlreadyRegisteredException.class, () -> userService.registerUser(userRequestDTO));
        }

        @Test
        void registerUser_invalidEmailFormat() {
            userRequestDTO = new UserRequestDTO("Test User", "invalid-email", "Password123!", Collections.emptyList());
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            assertThrows(InvalidEmailFormatException.class, () -> userService.registerUser(userRequestDTO));
        }

        @Test
        void getAllUsers_success() {
            when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

            List<UserResponseDTO> users = userService.getAllUsers();

            assertFalse(users.isEmpty());
            assertEquals(1, users.size());
        }

        @Test
        void getAllUsers_throwsResourceNotFoundException_whenNoUsersExist() {
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
        }

        @Test
        void getUserById_success() {
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

            UserResponseDTO response = userService.getUserById(user.getId());

            assertNotNull(response);
            assertEquals(user.getId(), response.getId());
        }

        @Test
        void getUserById_notFound() {
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(UUID.randomUUID()));
        }

        @Test
        void deleteUser_success() {
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

            UserResponseDTO response = userService.deleteUser(user.getId());

            assertNotNull(response);
        }

        @Test
        void deleteUser_notFound() {
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(UUID.randomUUID()));
        }

        @Test
        void patchUser_success() {
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO("Updated Name", null, null, null);

            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build());

            UserResponseDTO response = userService.patchUser(user.getId(), patchDTO);

            assertNotNull(response);
            assertEquals("Updated Name", user.getName());
        }

        @Test
        void patchUser_notFound() {
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, null, null, null);
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.patchUser(UUID.randomUUID(), patchDTO));
        }

        @Test
        void patchUser_invalidEmail() {
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, "invalid-email", null, null);

            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

            assertThrows(InvalidEmailFormatException.class, () -> userService.patchUser(user.getId(), patchDTO));
        }

        @Test
        void patchUser_emailAlreadyRegistered() {
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, "existing@example.com", null, null);

            User anotherUser = new User();
            anotherUser.setId(UUID.randomUUID());
            anotherUser.setEmail("existing@example.com");

            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(anotherUser));

            assertThrows(EmailAlreadyRegisteredException.class, () -> userService.patchUser(user.getId(), patchDTO));
        }

        @Test
        void patchUser_updatePhone() {
            UUID phoneId = UUID.randomUUID();
            Phone phone = new Phone();
            phone.setId(phoneId);
            phone.setNumber("111");
            user.getPhones().add(phone);

            PhonePatchRequestDTO phonePatch = new PhonePatchRequestDTO(phoneId, "222", null, null);
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, null, null, Collections.singletonList(phonePatch));

            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build());

            userService.patchUser(user.getId(), patchDTO);

            assertEquals("222", user.getPhones().get(0).getNumber());
        }

        @Test
        void patchUser_addPhone() {
            PhonePatchRequestDTO phonePatch = new PhonePatchRequestDTO(null, "333", null, null);
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, null, null, Collections.singletonList(phonePatch));

            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build());

            userService.patchUser(user.getId(), patchDTO);

            assertEquals(1, user.getPhones().size());
            assertEquals("333", user.getPhones().get(0).getNumber());
        }

        @Test
        void patchUser_phoneNotFound() {
            PhonePatchRequestDTO phonePatch = new PhonePatchRequestDTO(UUID.randomUUID(), null, null, null); // Non-existent phone ID
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, null, null, Collections.singletonList(phonePatch));
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            assertThrows(ResourceNotFoundException.class, () -> userService.patchUser(user.getId(), patchDTO));
        }

        @Test
        void patchUser_updatePassword() {
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, null, "newPassword123!", null);
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build());
            userService.patchUser(user.getId(), patchDTO);
            assertEquals("encodedNewPassword", user.getPassword());
        }

        @Test
        void patchUser_updateEmailToNewAndSame() {
            UserPatchRequestDTO patchDTO = new UserPatchRequestDTO(null, "new-email@example.com", null, Collections.emptyList());
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
            when(userRepository.findByEmail("new-email@example.com")).thenReturn(Optional.empty()); // Email doesn't exist
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);
            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build());
            userService.patchUser(user.getId(), patchDTO);
            assertEquals("new-email@example.com", user.getEmail());


            user.setEmail("test@example.com");
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user)); // Email belongs to the same user
            UserPatchRequestDTO sameEmailPatch = new UserPatchRequestDTO(null, "test@example.com", null, null);
            assertDoesNotThrow(() -> userService.patchUser(user.getId(), sameEmailPatch));

        }

        @Test
        void patchUser_updatePhoneCityAndCountryCode() {

            UUID phoneId = UUID.randomUUID();

            Phone phone = new Phone();

            phone.setId(phoneId);

            phone.setNumber("111");

            phone.setCitycode("10");

            phone.setCountrycode("57");

            user.getPhones().add(phone);

            // --- Test 1: Update citycode ---

            PhonePatchRequestDTO cityPatch = new PhonePatchRequestDTO(phoneId, null, "20", null);

            UserPatchRequestDTO patchDTO1 = new UserPatchRequestDTO(null, null, null, Collections.singletonList(cityPatch));

            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

            when(userRepository.save(any(User.class))).thenReturn(user);

            when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

            when(userDetailsService.loadUserByUsername(anyString())).thenReturn(org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).build());

            userService.patchUser(user.getId(), patchDTO1);

            assertEquals("20", user.getPhones().get(0).getCitycode());

            // --- Test 2: Update countrycode ---

            PhonePatchRequestDTO countryPatch = new PhonePatchRequestDTO(phoneId, null, null, "58");
            UserPatchRequestDTO patchDTO2 = new UserPatchRequestDTO(null, null, null, Collections.singletonList(countryPatch));
            userService.patchUser(user.getId(), patchDTO2);
            assertEquals("58", user.getPhones().get(0).getCountrycode());

        }
}