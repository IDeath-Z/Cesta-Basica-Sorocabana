package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;

@Component
public final class SurveyTypeMapper {

    public SurveyTypeJpaEntity toJpa(SurveyType surveyType) {
        SurveyTypeJpaEntity entity = new SurveyTypeJpaEntity();
        entity.setId(surveyType.getId());
        entity.setCode(surveyType.getCode());
        entity.setName(surveyType.getName());
        entity.setDescription(surveyType.getDescription());
        entity.setActive(surveyType.isActive());
        return entity;
    }

    public SurveyType toDomain(SurveyTypeJpaEntity entity) {
        return SurveyType.restore(
            entity.getId(),
            entity.getCode(),
            entity.getName(),
            entity.getDescription(),
            entity.isActive()
        );
    }
}
