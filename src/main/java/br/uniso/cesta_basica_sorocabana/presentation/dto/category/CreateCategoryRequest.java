package br.uniso.cesta_basica_sorocabana.presentation.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateCategoryRequest", description = "Payload for creating a new category")
public record CreateCategoryRequest(
    @NotBlank @Size(max = 120) @Schema(description = "Category name, must be unique", example = "Alimentação")
    String name
) {}
