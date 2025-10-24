package com.bci.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "Representa los datos de un teléfono que se devuelven en una respuesta de la API.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneResponseDTO {

    @Schema(description = "El ID único del teléfono.", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID id;

    @Schema(description = "El número de teléfono.", example = "1234567")
    private String number;

    @Schema(description = "El código de la ciudad para el número de teléfono.", example = "1")
    private String citycode;

    @Schema(description = "El código del país para el número de teléfono.", example = "57")
    private String countrycode;
}