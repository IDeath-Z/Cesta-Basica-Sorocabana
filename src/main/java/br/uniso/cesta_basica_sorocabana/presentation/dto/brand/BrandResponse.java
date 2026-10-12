package br.uniso.cesta_basica_sorocabana.presentation.dto.brand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.brand.BrandOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BrandResponse", description = "A product brand")
public record BrandResponse(
    @Schema(description = "Unique brand identifier", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID id,

    @Schema(description = "Brand name", example = "Camil")
    String name,

    @Schema(description = "Whether the brand can be used in new records", example = "true")
    boolean active
) {
    public static BrandResponse from(BrandOutput output) {
        return new BrandResponse(output.id(), output.name(), output.active());
    }
}
