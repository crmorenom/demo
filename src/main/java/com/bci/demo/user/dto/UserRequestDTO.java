package com.bci.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import com.bci.demo.cfg.ValidPassword;

@Schema(description = "Representa los datos necesarios para crear o actualizar un usuario.")
@Data
@AllArgsConstructor
public class UserRequestDTO {

    @Schema(description = "Nombre del usuario.", example = "Juan Rodriguez", required = true)
    @NotBlank(message = "Nombre requerido")
    @Size(max = 100, message = "nombre debe tener como máximo 100 caracteres")
    private String name;

    @Schema(description = "Correo electrónico del usuario. Debe ser único.", example = "juan@rodriguez.org", required = true)
    @NotBlank(message = "Correo electrónico requerido")
    @Size(max = 100, message = "Correo electrónico debe tener como máximo 100 caracteres")
    @Email
    private String email;

    @Schema(description = "Password para la cuenta del usuario. Debe cumplir con los requisitos de complejidad configurados.", example = "hunter2", required = true)
    @NotBlank(message = "Password requerida")
    @ValidPassword
    private String password;

    @Schema(description = "Lista de teléfonos asociados al usuario.")
    @Valid
    @NotNull(message = "Se requiere al menos un teléfono")
    private List<PhoneRequestDTO> phones;
}