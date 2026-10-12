package br.uniso.cesta_basica_sorocabana.presentation.dto.product;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateProductRequest", description = "Payload for updating a product. Omitted (null) fields keep their current value")
public record UpdateProductRequest(
    @Schema(description = "ID of an active category", example = "5b2e7c1d-9f4a-4d3b-8e6c-2a1f0b9c8d7e")
    UUID categoryId,

    @Schema(description = "ID of an active sales unit", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d")
    UUID unitId,

    @Size(min = 1, max = 120) @Schema(description = "Product name", example = "Arroz")
    String name,

    @Size(max = 120) @Schema(description = "Specification", example = "Tipo 2")
    String specification,

    @Size(max = 500) @Schema(description = "Free notes", example = "Parboiled rice")
    String notes
) {}
