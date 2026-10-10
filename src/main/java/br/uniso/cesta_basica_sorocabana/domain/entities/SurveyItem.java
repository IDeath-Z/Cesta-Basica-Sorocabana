package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.SurveyItemError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class SurveyItem {

    private final UUID id;
    private final UUID surveyId;
    private final UUID productBrandId;

    private SurveyItem(UUID id, UUID surveyId, UUID productBrandId) {
        this.id = DomainValidator.requireNonNull(id, SurveyItemError.ID_REQUIRED.message());
        this.surveyId = DomainValidator.requireNonNull(surveyId, SurveyItemError.SURVEY_ID_REQUIRED.message());
        this.productBrandId = DomainValidator.requireNonNull(productBrandId, SurveyItemError.PRODUCT_BRAND_ID_REQUIRED.message());
    }

    public static SurveyItem createNew(UUID surveyId, UUID productBrandId) {
        return new SurveyItem(UUID.randomUUID(), surveyId, productBrandId);
    }

    public static SurveyItem restore(UUID id, UUID surveyId, UUID productBrandId) {
        return new SurveyItem(id, surveyId, productBrandId);
    }

    public UUID getId() {
        return id;
    }

    public UUID getSurveyId() {
        return surveyId;
    }

    public UUID getProductBrandId() {
        return productBrandId;
    }
}
