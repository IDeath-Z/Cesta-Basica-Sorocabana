package br.uniso.cesta_basica_sorocabana.presentation.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateStoreRequest", description = "Payload for creating a new store")
public record CreateStoreRequest(
    @NotBlank @Size(max = 120) @Schema(description = "Store name, must be unique", example = "Tauste")
    String name
) {}
