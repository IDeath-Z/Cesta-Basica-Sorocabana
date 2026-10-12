package br.uniso.cesta_basica_sorocabana.presentation.dto.unit;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateUnitRequest", description = "Payload for creating a sales unit. The pair quantity + measure must be unique")
public record CreateUnitRequest(
    @NotNull @DecimalMin(value = "0", inclusive = false) @Digits(integer = 8, fraction = 4)
    @Schema(description = "Amount sold, greater than zero, up to 4 decimal places", example = "5")
    BigDecimal quantity,

    @NotBlank @Size(max = 20) @Schema(description = "Unit of measure", example = "kg")
    String measure
) {}
