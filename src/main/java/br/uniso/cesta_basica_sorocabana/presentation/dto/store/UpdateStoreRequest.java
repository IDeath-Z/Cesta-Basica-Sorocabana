package br.uniso.cesta_basica_sorocabana.presentation.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateStoreRequest", description = "Payload for renaming a store")
public record UpdateStoreRequest(
    @NotBlank @Size(max = 120) @Schema(description = "New store name, must be unique", example = "Tauste Supermercados")
    String name
) {}
