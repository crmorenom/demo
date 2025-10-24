package com.bci.demo.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bci.demo.user.dto.ApiResponseDTO;
import com.bci.demo.user.dto.LoginRequestDTO;
import com.bci.demo.user.dto.LoginResponseDTO;
import com.bci.demo.user.dto.UserPatchRequestDTO;
import com.bci.demo.user.dto.UserRequestDTO;
import com.bci.demo.user.dto.UserResponseDTO;
import com.bci.demo.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Controlador de Usuarios")
public class UserController {
        @Autowired
        private UserService userService;

        @Operation(summary = "Login de usuario", security = {}, description = "Autentica al usuario y devuelve un token JWT junto con información del usuario.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Usuario autenticado exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))),
                @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content),
                
        })
        @PostMapping("/login")
        public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
                @Parameter(description = "Datos para iniciar sesión") @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
                LoginResponseDTO loginResponse = userService.login(loginRequestDTO);
                ApiResponseDTO<LoginResponseDTO> response = new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Usuario autenticado exitosamente",
                        loginResponse
                );
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Registrar un nuevo usuario", security = {}, description = "Crea un nuevo usuario y devuelve los detalles del usuario creado junto con un token JWT.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))),
                @ApiResponse(responseCode = "400", description = "Formato de email inválido", content = @Content),
                @ApiResponse(responseCode = "409", description = "El correo ya está registrado", content = @Content)
        })
        @PostMapping("/register")
        public ResponseEntity<ApiResponseDTO<UserResponseDTO>> registerUser(
                @Parameter(description = "Detalles del usuario para el registro") @Valid @RequestBody UserRequestDTO userRequestDTO) {
                UserResponseDTO registeredUser = userService.registerUser(userRequestDTO);
                ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                        HttpStatus.CREATED.value(),
                        "Usuario creado exitosamente",
                        registeredUser
                );
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @Operation(summary = "Obtener todos los usuario", description = "Devuelve una lista de todos los usuarios.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
        })
        @GetMapping
        public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsers() {
                List<UserResponseDTO> users = userService.getAllUsers();
                ApiResponseDTO<List<UserResponseDTO>> response = new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Usuarios obtenidos exitosamente",
                        users
                );
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Obtener un usuario por ID", description = "Devuelve un único usuario.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(
                @Parameter(description = "ID del usuario a obtener") @PathVariable UUID id) {
                UserResponseDTO user = userService.getUserById(id);
                ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Usuario obtenido exitosamente",
                        user
                );
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario por su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponseDTO<UserResponseDTO>> deleteUser(
                @Parameter(description = "ID del usuario a eliminar") @PathVariable UUID id) {
                UserResponseDTO user = userService.deleteUser(id);
                ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Usuario eliminado exitosamente",
                        user
                );
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Actualizar un usuario", description = "Permite modificar un usuario existente, incluyendo teléfonos. Si se envía un teléfono con ID existente, se actualiza; si no tiene ID, se agrega uno nuevo.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))),
                @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
                @ApiResponse(responseCode = "404", description = "Usuario o teléfono no encontrado", content = @Content)
        })
        @PatchMapping("/{id}")
        public ResponseEntity<ApiResponseDTO<UserResponseDTO>> patchUser(
                @Parameter(description = "ID del usuario a actualizar parcialmente") @PathVariable UUID id,
                @Parameter(description = "Datos parciales del usuario") @Valid @RequestBody UserPatchRequestDTO userPatchRequestDTO) {
                UserResponseDTO updatedUser = userService.patchUser(id, userPatchRequestDTO);
                ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Usuario actualizado exitosamente",
                        updatedUser
                );

                return ResponseEntity.ok(response);
        }
}
