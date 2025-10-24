package com.bci.demo.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bci.demo.cfg.security.JwtUtil;
import com.bci.demo.handlers.exceptions.EmailAlreadyRegisteredException;
import com.bci.demo.handlers.exceptions.InvalidCredentialsException;
import com.bci.demo.handlers.exceptions.InvalidEmailFormatException;
import com.bci.demo.handlers.exceptions.ResourceNotFoundException;
import com.bci.demo.user.dto.LoginRequestDTO;
import com.bci.demo.user.dto.LoginResponseDTO;
import com.bci.demo.user.dto.PhonePatchRequestDTO;
import com.bci.demo.user.dto.UserPatchRequestDTO;
import com.bci.demo.user.dto.UserRequestDTO;
import com.bci.demo.user.dto.UserResponseDTO;
import com.bci.demo.user.entity.Phone;
import com.bci.demo.user.entity.User;
import com.bci.demo.user.mapper.UserMapper;
import com.bci.demo.user.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String TELEFONO_NO_ENCONTRADO_CON_ID = "Teléfono no encontrado con ID: ";
    private static final String USUARIO_NO_ENCONTRADO_CON_ID = "Usuario no encontrado con ID: ";
    private static final String NO_SE_ENCONTRARON_USUARIOS = "No se encontraron usuarios";
    private static final String FORMATO_DE_EMAIL_INVALIDO = "Formato de email inválido";
    private static final String EL_CORREO_YA_ESTA_REGISTRADO = "El correo ya está registrado";
    private static final String CREDENCIALES_INVALIDAS = "Credenciales inválidas";

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException(CREDENCIALES_INVALIDAS));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(CREDENCIALES_INVALIDAS);
        }

        user.setLastLogin(LocalDateTime.now());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        user.setToken(token);
        userRepository.save(user);

        return new LoginResponseDTO(
                user.getId(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                token,
                user.getIsActive()
        );
    }

    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        ensureEmailNotRegistered(userRequestDTO.getEmail());
        ensureValidEmailFormat(userRequestDTO.getEmail());

        User user = buildNewUserFromRequest(userRequestDTO);
        User savedUser = userRepository.save(user);

        savedUser = generateAndSaveToken(savedUser);

        return userMapper.toResponseDTO(savedUser);
    }

    private void ensureEmailNotRegistered(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException(EL_CORREO_YA_ESTA_REGISTRADO);
        }
    }

    private void ensureValidEmailFormat(String email) {
        if (!isValidEmail(email)) {
            throw new InvalidEmailFormatException(FORMATO_DE_EMAIL_INVALIDO);
        }
    }

    private User buildNewUserFromRequest(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setIsActive(true);

        if (dto.getPhones() != null) {
            user.setPhones(dto.getPhones().stream().map(phoneDTO -> {
                Phone phone = new Phone();
                phone.setNumber(phoneDTO.number());
                phone.setCitycode(phoneDTO.citycode());
                phone.setCountrycode(phoneDTO.countrycode());
                phone.setUser(user);
                return phone;
            }).collect(Collectors.toList()));
        }

        return user;
    }

    private User generateAndSaveToken(User savedUser) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        savedUser.setToken(token);
        return userRepository.save(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
        if (users.isEmpty()) {
            throw new ResourceNotFoundException(NO_SE_ENCONTRARON_USUARIOS);
        }
        return users;
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NO_ENCONTRADO_CON_ID + id));
    }

    @Override
    public UserResponseDTO deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NO_ENCONTRADO_CON_ID + id));

        UserResponseDTO deletedUser = userMapper.toResponseDTO(user);
        userRepository.delete(user);
        return deletedUser;
    }

    @Override
    public UserResponseDTO patchUser(UUID id, UserPatchRequestDTO userPatchDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NO_ENCONTRADO_CON_ID + id));

        updateEmailIfPresent(user, userPatchDTO);
        updateNameIfPresent(user, userPatchDTO);
        updatePasswordIfPresent(user, userPatchDTO);
        updatePhonesIfPresent(user, userPatchDTO);

        user.setModified(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(savedUser.getEmail()));
        savedUser.setToken(token);

        return userMapper.toResponseDTO(userRepository.save(savedUser));
    }

    private void updateEmailIfPresent(User user, UserPatchRequestDTO dto) {
        if (dto.getEmail() != null) {
            if (!isValidEmail(dto.getEmail())) {
                throw new InvalidEmailFormatException(FORMATO_DE_EMAIL_INVALIDO);
            }
            userRepository.findByEmail(dto.getEmail())
                    .filter(u -> !u.getId().equals(user.getId()))
                    .ifPresent(u -> { throw new EmailAlreadyRegisteredException(EL_CORREO_YA_ESTA_REGISTRADO); });
            user.setEmail(dto.getEmail());
        }
    }

    private void updateNameIfPresent(User user, UserPatchRequestDTO dto) {
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
    }

    private void updatePasswordIfPresent(User user, UserPatchRequestDTO dto) {
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }

    private void updatePhonesIfPresent(User user, UserPatchRequestDTO dto) {
        if (dto.getPhones() != null && !dto.getPhones().isEmpty()) {
            for (PhonePatchRequestDTO phoneDTO : dto.getPhones()) {
                if (phoneDTO.id() != null) {
                    Phone phone = user.getPhones().stream()
                            .filter(p -> p.getId().equals(phoneDTO.id()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException(TELEFONO_NO_ENCONTRADO_CON_ID + phoneDTO.id()));
                    if (phoneDTO.number() != null) phone.setNumber(phoneDTO.number());
                    if (phoneDTO.citycode() != null) phone.setCitycode(phoneDTO.citycode());
                    if (phoneDTO.countrycode() != null) phone.setCountrycode(phoneDTO.countrycode());
                } else {
                    Phone newPhone = new Phone();
                    newPhone.setNumber(phoneDTO.number());
                    newPhone.setCitycode(phoneDTO.citycode());
                    newPhone.setCountrycode(phoneDTO.countrycode());
                    newPhone.setUser(user);
                    user.getPhones().add(newPhone);
                }
            }
        }
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}