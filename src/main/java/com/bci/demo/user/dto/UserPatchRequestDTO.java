package com.bci.demo.user.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Representa los datos opcionales para actualizar parcialmente un usuario (PATCH).")
@Data
@AllArgsConstructor
public class UserPatchRequestDTO {

    @Schema(description = "Nombre del usuario.", example = "Juan Rodriguez", required = false)
    @Size(max = 100, message = "El nombre debe tener como máximo 100 caracteres")
    @Pattern(regexp = "^(?!\\s*$).+", message = "El nombre no puede estar vacío")
    private String name;

    @Schema(description = "Correo electrónico del usuario. Debe ser único.", example = "juan@rodriguez.org", required = false)
    @Size(max = 100, message = "El correo electrónico debe tener como máximo 100 caracteres")
    @Email(message = "Formato de correo electrónico inválido")
    @Pattern(regexp = "^(?!\\s*$).+", message = "El correo electrónico no puede estar vacío")
    private String email;

    @Schema(description = "Password del usuario. Debe cumplir los requisitos de complejidad.", example = "Hunter22", required = false)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d.*\\d)[A-Za-z\\d]{8,12}$", message = "La contraseña debe tener una mayúscula, dos números y una longitud entre 8 y 12 caracteres")
    private String password;

    @Schema(description = "Lista de teléfonos asociados al usuario (si se desea actualizar o agregar nuevos).")
    @Valid
    private List<PhonePatchRequestDTO> phones;
}