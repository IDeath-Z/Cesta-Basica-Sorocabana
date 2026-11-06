package br.uniso.cesta_basica_sorocabana.presentation.dto.productbrandstore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateProductBrandStoreRequest", description = "Payload for changing the search name of a basket item in a store")
public record UpdateProductBrandStoreRequest(
    @NotBlank @Size(max = 200) @Schema(description = "How this store names the item, brand included", example = "Arroz Camil 5kg tipo 1")
    String searchName
) {}
