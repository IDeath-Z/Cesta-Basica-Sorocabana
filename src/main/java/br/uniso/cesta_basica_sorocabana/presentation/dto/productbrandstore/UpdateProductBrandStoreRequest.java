package br.uniso.cesta_basica_sorocabana.presentation.dto.productbrandstore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateProductBrandStoreRequest", description = "Payload for changing the search name of a basket item in a store")
public record UpdateProductBrandStoreRequest(
    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio; use nulo quando o mercado não tem o item") @Size(max = 200)
    @Schema(description = "How this store names the item, brand included. Null when the store does not carry the item (it is not searched there)", example = "Arroz Camil 5kg tipo 1", nullable = true)
    String searchName
) {}
