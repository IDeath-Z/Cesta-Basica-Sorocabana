package br.uniso.cesta_basica_sorocabana.presentation.dto.survey;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "SaveSurveyPricesRequest", description = "Prices of one or more stores. Send one item or the whole screen: items not sent stay as they are")
public record SaveSurveyPricesRequest(
    @NotEmpty @Valid @Schema(description = "Stores with their items")
    List<StoreEntry> stores
) {
    @Schema(name = "SaveSurveyPricesStore", description = "One store and its items")
    public record StoreEntry(
        @NotNull @Schema(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
        UUID storeId,

        @NotEmpty @Valid @Schema(description = "Items of this store")
        List<ItemEntry> items
    ) {}

    // The price must be present in the JSON, so a field forgotten by the client does not clear a saved price.
    @Schema(name = "SaveSurveyPricesItem", description = "One item and its price")
    public record ItemEntry(
        @NotNull @Schema(description = "Product brand ID", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b")
        UUID productBrandId,

        @JsonProperty(required = true) @DecimalMin("0.01") @Digits(integer = 8, fraction = 2)
        @Schema(description = "Price, greater than 0, with up to 2 decimal places. Null clears the item in this store", example = "25.90", nullable = true)
        BigDecimal price
    ) {}
}
