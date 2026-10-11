package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitemprice;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItemPrice;

@Component
public final class SurveyItemPriceMapper {

    public SurveyItemPriceJpaEntity toJpa(SurveyItemPrice surveyItemPrice) {
        SurveyItemPriceJpaId id = new SurveyItemPriceJpaId(surveyItemPrice.getSurveyItemId(), surveyItemPrice.getStoreId());

        SurveyItemPriceJpaEntity entity = new SurveyItemPriceJpaEntity();
        entity.setId(id);
        entity.setPrice(surveyItemPrice.getPrice());
        return entity;
    }

    public SurveyItemPrice toDomain(SurveyItemPriceJpaEntity entity) {
        return SurveyItemPrice.restore(
            entity.getId().getSurveyItemId(),
            entity.getId().getStoreId(),
            entity.getPrice()
        );
    }
}
