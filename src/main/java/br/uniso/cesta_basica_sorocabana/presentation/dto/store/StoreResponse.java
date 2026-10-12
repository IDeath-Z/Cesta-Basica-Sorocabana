package br.uniso.cesta_basica_sorocabana.presentation.dto.store;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.store.StoreOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StoreResponse", description = "A product store")
public record StoreResponse(
    @Schema(description = "Unique store identifier", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID id,

    @Schema(description = "Store name", example = "Tauste")
    String name,

    @Schema(description = "Whether the store can be used in new records", example = "true")
    boolean active
) {
    public static StoreResponse from(StoreOutput output) {
        return new StoreResponse(output.id(), output.name(), output.active());
    }
}
