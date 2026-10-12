package br.uniso.cesta_basica_sorocabana.presentation.dto.unit;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.unit.UnitOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UnitResponse", description = "How a product is sold, e.g. 5 kg, 200 g, 8 un")
public record UnitResponse(
    @Schema(description = "Unique unit identifier", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d")
    UUID id,

    @Schema(description = "Amount sold", example = "5")
    BigDecimal quantity,

    @Schema(description = "Unit of measure", example = "kg")
    String measure,

    @Schema(description = "Whether the unit can be used by new products", example = "true")
    boolean active
) {
    public static UnitResponse from(UnitOutput output) {
        return new UnitResponse(output.id(), output.quantity(), output.measure(), output.active());
    }
}
