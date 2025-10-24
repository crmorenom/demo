package com.bci.demo.user.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Representa un teléfono que puede ser actualizado parcialmente o agregado a un usuario.")
public record PhonePatchRequestDTO(

    @Schema(description = "ID del teléfono existente (si se va a actualizar).", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "Número del teléfono.", example = "1234567", required = false)
    @Pattern(regexp = "^(?!\\s*$).+", message = "El número de teléfono no puede estar vacío")
    String number,

    @Schema(description = "Código de la ciudad.", example = "1", required = false)
    @Pattern(regexp = "^(?!\\s*$).+", message = "El código de ciudad no puede estar vacío")
    String citycode,

    @Schema(description = "Código del país.", example = "57", required = false)
    @Pattern(regexp = "^(?!\\s*$).+", message = "El código de país no puede estar vacío")
    String countrycode
) {}
