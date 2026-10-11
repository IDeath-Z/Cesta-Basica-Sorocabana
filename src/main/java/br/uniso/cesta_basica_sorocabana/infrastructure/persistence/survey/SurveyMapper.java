package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.survey;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.surveytype.SurveyTypeJpaEntity;

@Component
public final class SurveyMapper {

    public SurveyJpaEntity toJpa(Survey survey, SurveyTypeJpaEntity surveyTypeJpaEntity) {
        SurveyJpaEntity entity = new SurveyJpaEntity();
        entity.setId(survey.getId());
        entity.setSurveyDate(survey.getSurveyDate());
        entity.setSurveyType(surveyTypeJpaEntity);
        entity.setNotes(survey.getNotes());
        entity.setDraft(survey.isDraft());
        entity.setCreatedAt(survey.getCreatedAt());
        return entity;
    }

    public Survey toDomain(SurveyJpaEntity entity) {
        return Survey.restore(
            entity.getId(),
            entity.getSurveyDate(),
            entity.getSurveyType().getId(),
            entity.getNotes(),
            entity.isDraft(),
            entity.getCreatedAt()
        );
    }
}
