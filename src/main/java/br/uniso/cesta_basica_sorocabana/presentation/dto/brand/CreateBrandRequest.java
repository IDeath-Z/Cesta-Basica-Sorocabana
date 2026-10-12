package br.uniso.cesta_basica_sorocabana.presentation.dto.brand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateBrandRequest", description = "Payload for creating a new brand")
public record CreateBrandRequest(
    @NotBlank @Size(max = 120) @Schema(description = "Brand name, must be unique", example = "Camil")
    String name
) {}
