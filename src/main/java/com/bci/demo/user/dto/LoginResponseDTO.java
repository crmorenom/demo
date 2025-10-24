package com.bci.demo.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa los datos de un inicio de sesión que se devuelven en la respuesta de la API.")
public class LoginResponseDTO {

    @Schema(description = "Identificador único del usuario (UUID generado por la base de datos).", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(description = "Fecha de creación del usuario.", example = "2025-10-23T15:30:00")
    private LocalDateTime created;

    @Schema(description = "Fecha de la última modificación del usuario.", example = "2025-10-23T15:30:00")
    private LocalDateTime modified;

    @Schema(description = "Fecha del último inicio de sesión del usuario.", example = "2025-10-23T15:30:00")
    private LocalDateTime lastLogin;

    @Schema(description = "Token de acceso para consumir la API (JWT o UUID).", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Indica si el usuario sigue activo en el sistema.", example = "true")
    private Boolean isActive;
}