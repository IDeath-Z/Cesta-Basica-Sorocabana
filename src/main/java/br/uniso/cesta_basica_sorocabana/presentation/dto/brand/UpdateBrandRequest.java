package br.uniso.cesta_basica_sorocabana.presentation.dto.brand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateBrandRequest", description = "Payload for renaming a brand")
public record UpdateBrandRequest(
    @NotBlank @Size(max = 120) @Schema(description = "New brand name, must be unique", example = "Camil Alimentos")
    String name
) {}
