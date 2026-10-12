package br.uniso.cesta_basica_sorocabana.presentation.dto.productbrand;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateProductBrandRequest", description = "Payload for linking a brand to a product")
public record CreateProductBrandRequest(
    @NotNull @Schema(description = "ID of an active brand", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID brandId
) {}
