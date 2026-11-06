package br.uniso.cesta_basica_sorocabana.presentation.dto.productbrandstore;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore.ProductBrandStoreOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductBrandStoreResponse", description = "A basket item in a store. Without this link the item is not surveyed in that store")
public record ProductBrandStoreResponse(
    @Schema(description = "Unique link identifier", example = "b7a6c5d4-e3f2-4a1b-9c8d-7e6f5a4b3c2d")
    UUID id,

    @Schema(description = "Product brand ID", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b")
    UUID productBrandId,

    @Schema(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID storeId,

    @Schema(description = "How this store names the item, brand included", example = "Arroz Camil tipo 1 5kg")
    String searchName,

    @Schema(description = "Whether the item is surveyed in this store", example = "true")
    boolean active
) {
    public static ProductBrandStoreResponse from(ProductBrandStoreOutput output) {
        return new ProductBrandStoreResponse(output.id(), output.productBrandId(), output.storeId(), output.searchName(), output.active());
    }
}
