package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.SurveyItemPriceError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class SurveyItemPrice {

    private final UUID surveyItemId;
    private final UUID storeId;
    private final BigDecimal price;

    private SurveyItemPrice(UUID surveyItemId, UUID storeId, BigDecimal price) {
        this.surveyItemId = DomainValidator.requireNonNull(surveyItemId, SurveyItemPriceError.SURVEY_ITEM_ID_REQUIRED.message());
        this.storeId = DomainValidator.requireNonNull(storeId, SurveyItemPriceError.STORE_ID_REQUIRED.message());
        this.price = DomainValidator.requirePositive(price, SurveyItemPriceError.PRICE_NOT_POSITIVE.message());
    }

    public static SurveyItemPrice createNew(UUID surveyItemId, UUID storeId, BigDecimal price) {
        return new SurveyItemPrice(surveyItemId, storeId, price);
    }

    public static SurveyItemPrice restore(UUID surveyItemId, UUID storeId, BigDecimal price) {
        return new SurveyItemPrice(surveyItemId, storeId, price);
    }

    public UUID getSurveyItemId() {
        return surveyItemId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public SurveyItemPrice updatePrice(BigDecimal price) {
        return new SurveyItemPrice(this.surveyItemId, this.storeId, price);
    }
}
