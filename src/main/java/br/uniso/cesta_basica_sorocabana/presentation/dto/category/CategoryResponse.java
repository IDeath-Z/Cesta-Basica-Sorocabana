package br.uniso.cesta_basica_sorocabana.presentation.dto.category;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.category.CategoryOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CategoryResponse", description = "A product category")
public record CategoryResponse(
    @Schema(description = "Unique category identifier", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID id,

    @Schema(description = "Category name", example = "Alimentação")
    String name,

    @Schema(description = "Whether the category can be used in new records", example = "true")
    boolean active
) {
    public static CategoryResponse from(CategoryOutput output) {
        return new CategoryResponse(output.id(), output.name(), output.active());
    }
}
