package br.uniso.cesta_basica_sorocabana.presentation.dto.store;

import org.hibernate.validator.constraints.URL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateStoreRequest", description = "Payload for updating a store. Omitted (null) fields keep their current value")
public record UpdateStoreRequest(
    @Size(min = 1, max = 120) @Schema(description = "New store name, must be unique", example = "Tauste Supermercados")
    String name,

    @URL @Size(max = 2048) @Schema(description = "Store website", example = "https://tauste.com.br/sorocaba1")
    String storeUrl
) {}
