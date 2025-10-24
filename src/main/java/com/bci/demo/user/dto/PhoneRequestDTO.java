package com.bci.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa un número de teléfono asociado a un usuario.")
public record PhoneRequestDTO(
    @Schema(description = "El número de teléfono.", example = "1234567", required = true)
    @NotBlank(message = "El número de teléfono es requerido")
    String number,

    @Schema(description = "El código de la ciudad para el número de teléfono.", example = "1", required = true)
    @NotBlank(message = "El código de la ciudad es requerido")
    String citycode,

    @Schema(description = "El código del país para el número de teléfono.", example = "57", required = true)
    @NotBlank(message = "El código del país es requerido")
    String countrycode
) {}

