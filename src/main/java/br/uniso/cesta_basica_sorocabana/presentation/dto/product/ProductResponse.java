package br.uniso.cesta_basica_sorocabana.presentation.dto.product;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.product.ProductOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductResponse", description = "A product of the basic food basket, e.g. rice, toothpaste")
public record ProductResponse(
    @Schema(description = "Unique product identifier", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a")
    UUID id,

    @Schema(description = "Category ID", example = "5b2e7c1d-9f4a-4d3b-8e6c-2a1f0b9c8d7e")
    UUID categoryId,

    @Schema(description = "Sales unit ID, null when not defined", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d")
    UUID unitId,

    @Schema(description = "Product name", example = "Arroz")
    String name,

    @Schema(description = "Specification", example = "Tipo 1")
    String specification,

    @Schema(description = "Free notes", example = "Long grain white rice")
    String notes,

    @Schema(description = "Whether the product can be used in new records", example = "true")
    boolean active
) {
    public static ProductResponse from(ProductOutput output) {
        return new ProductResponse(output.id(), output.categoryId(), output.unitId(), output.name(),
            output.specification(), output.notes(), output.active());
    }
}
