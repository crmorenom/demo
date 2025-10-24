package com.bci.demo.user.dto;

import com.bci.demo.cfg.ValidPassword;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para iniciar sesión en la API.")
public class LoginRequestDTO {
    @Schema(
        description = "Correo electrónico del usuario. Debe existir y ser único.",
        example = "juan@rodriguez.org",
        required = true
    )
    @NotBlank(message = "Correo electrónico requerido")
    @Size(max = 100, message = "Correo electrónico debe tener como máximo 100 caracteres")
    @Email(message = "Correo electrónico inválido")
    private String email;
    @Schema(
        description = "Password del usuario. Debe cumplir los requisitos de complejidad configurados.",
        example = "Hunter22",
        required = true
    )
    @NotBlank(message = "Password requerida")
    @ValidPassword
    private String password;
}