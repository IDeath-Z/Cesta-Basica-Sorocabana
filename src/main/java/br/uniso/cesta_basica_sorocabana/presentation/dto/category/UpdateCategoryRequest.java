package br.uniso.cesta_basica_sorocabana.presentation.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateCategoryRequest", description = "Payload for renaming a category")
public record UpdateCategoryRequest(
    @NotBlank @Size(max = 120) @Schema(description = "New category name, must be unique", example = "Alimentos")
    String name
) {}
