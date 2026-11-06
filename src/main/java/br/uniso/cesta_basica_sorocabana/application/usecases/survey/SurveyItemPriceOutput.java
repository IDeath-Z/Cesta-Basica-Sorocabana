package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItemPrice;

public record SurveyItemPriceOutput(UUID surveyItemId, UUID storeId, BigDecimal price) {

    public static SurveyItemPriceOutput from(SurveyItemPrice surveyItemPrice) {
        return new SurveyItemPriceOutput(surveyItemPrice.getSurveyItemId(), surveyItemPrice.getStoreId(), surveyItemPrice.getPrice());
    }
}
