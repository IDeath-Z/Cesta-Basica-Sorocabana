package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveyitem;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyItem;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey.SurveyJpaEntity;

@Component
public final class SurveyItemMapper {

    public SurveyItemJpaEntity toJpa(SurveyItem surveyItem, SurveyJpaEntity surveyJpaEntity, ProductBrandJpaEntity productBrandJpaEntity) {
        SurveyItemJpaEntity entity = new SurveyItemJpaEntity();
        entity.setId(surveyItem.getId());
        entity.setSurvey(surveyJpaEntity);
        entity.setProductBrand(productBrandJpaEntity);
        return entity;
    }

    public SurveyItem toDomain(SurveyItemJpaEntity entity) {
        return SurveyItem.restore(
            entity.getId(),
            entity.getSurvey().getId(),
            entity.getProductBrand().getId()
        );
    }
}
