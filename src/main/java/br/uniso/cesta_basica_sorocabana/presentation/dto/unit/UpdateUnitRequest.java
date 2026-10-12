package br.uniso.cesta_basica_sorocabana.presentation.dto.unit;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateUnitRequest", description = "Payload for updating a sales unit. Omitted (null) fields keep their current value")
public record UpdateUnitRequest(
    @DecimalMin(value = "0", inclusive = false) @Digits(integer = 8, fraction = 4)
    @Schema(description = "Amount sold, greater than zero, up to 4 decimal places", example = "1")
    BigDecimal quantity,

    @Size(min = 1, max = 20) @Schema(description = "Unit of measure", example = "kg")
    String measure
) {}
