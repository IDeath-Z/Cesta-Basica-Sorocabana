package br.uniso.cesta_basica_sorocabana.presentation.dto.product;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateProductRequest", description = "Payload for creating a product. The pair name + specification must be unique")
public record CreateProductRequest(
    @NotNull @Schema(description = "ID of an active category", example = "5b2e7c1d-9f4a-4d3b-8e6c-2a1f0b9c8d7e")
    UUID categoryId,

    @Schema(description = "ID of an active sales unit, optional", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d")
    UUID unitId,

    @NotBlank @Size(max = 120) @Schema(description = "Product name", example = "Arroz")
    String name,

    @Size(max = 120) @Schema(description = "Optional specification that tells products with the same name apart", example = "Tipo 1")
    String specification,

    @Size(max = 500) @Schema(description = "Optional free notes", example = "Long grain white rice")
    String notes
) {}
