package br.uniso.cesta_basica_sorocabana.presentation.dto.productbrandstore;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateProductBrandStoreRequest", description = "Payload for linking a basket item to a store")
public record CreateProductBrandStoreRequest(
    @NotNull @Schema(description = "ID of an active store", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID storeId,

    @NotBlank @Size(max = 200) @Schema(description = "How this store names the item, brand included", example = "Arroz Camil tipo 1 5kg")
    String searchName
) {}
