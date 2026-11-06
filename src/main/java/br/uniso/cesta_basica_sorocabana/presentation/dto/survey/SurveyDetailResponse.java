package br.uniso.cesta_basica_sorocabana.presentation.dto.survey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.survey.SurveyDetailOutput;
import br.uniso.cesta_basica_sorocabana.presentation.dto.basket.BasketItemResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SurveyDetailResponse", description = "A survey with the stores that already have prices and the items saved in each")
public record SurveyDetailResponse(
    @Schema(description = "Unique survey identifier", example = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d")
    UUID id,

    @Schema(description = "Survey date", example = "2026-11-10")
    LocalDate surveyDate,

    @Schema(description = "Survey type ID", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b")
    UUID surveyTypeId,

    @Schema(description = "Free notes", example = "Week of the holiday")
    String notes,

    @Schema(description = "True while the survey still accepts changes", example = "true")
    boolean draft,

    @Schema(description = "When the survey was created", example = "2026-11-10T14:15:36.625Z")
    Instant createdAt,

    @Schema(description = "Stores with at least one saved price")
    List<StoreEntry> stores
) {
    @Schema(name = "SurveyDetailStore", description = "A store and its saved items")
    public record StoreEntry(
        @Schema(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
        UUID storeId,

        @Schema(description = "Store name", example = "Tauste")
        String storeName,

        @Schema(description = "Store website", example = "https://tauste.com.br/sorocaba1")
        String storeUrl,

        @Schema(description = "Items saved for this store")
        List<BasketItemResponse> items
    ) {}

    public static SurveyDetailResponse from(SurveyDetailOutput output) {
        List<StoreEntry> stores = output.stores().stream()
            .map(store -> new StoreEntry(store.storeId(), store.storeName(), store.storeUrl(),
                store.items().stream().map(BasketItemResponse::from).toList()))
            .toList();

        return new SurveyDetailResponse(output.id(), output.surveyDate(), output.surveyTypeId(), output.notes(),
            output.draft(), output.createdAt(), stores);
    }
}
