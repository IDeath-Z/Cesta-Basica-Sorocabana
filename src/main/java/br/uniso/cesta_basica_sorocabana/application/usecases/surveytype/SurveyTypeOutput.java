package br.uniso.cesta_basica_sorocabana.application.usecases.surveytype;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;

public record SurveyTypeOutput(UUID id, String code, String name, String description, boolean active) {

    public static SurveyTypeOutput from(SurveyType surveyType) {
        return new SurveyTypeOutput(surveyType.getId(), surveyType.getCode(), surveyType.getName(),
            surveyType.getDescription(), surveyType.isActive());
    }
}
