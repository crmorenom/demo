package com.bci.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa los datos de un usuario que se devuelven en una respuesta de la API.")
public class UserResponseDTO {

    @Schema(description = "El ID único del usuario.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nombre del usuario.", example = "Juan Rodriguez")
    private String name;

    @Schema(description = "Correo electrónico del usuario.", example = "juan@rodriguez.org")
    private String email;

    @Schema(description = "Fecha y hora en que se creó el usuario.", example = "2023-10-27T10:00:00")
    private LocalDateTime created;

    @Schema(description = "Fecha y hora de la última modificación del usuario.", example = "2023-10-27T10:00:00")
    private LocalDateTime modified;

    @Schema(description = "Fecha y hora del último inicio de sesión del usuario.", example = "2023-10-27T10:00:00")
    private LocalDateTime lastLogin;

    @Schema(description = "Token de autenticación JWT para el usuario.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlei5vcmciLCJpYXQiOjE2MTYyMzkwMjIsImV4cCI6MTYxNjI0NTk5OX0.example-token")
    private String token;

    @Schema(description = "Indica si el usuario está activo.", example = "true")
    private boolean isActive;

    @Schema(description = "Lista de teléfonos asociados al usuario.")
    private List<PhoneResponseDTO> phones;
}