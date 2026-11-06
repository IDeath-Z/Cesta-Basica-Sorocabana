package br.uniso.cesta_basica_sorocabana.application.usecases.surveytype;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;

public record SurveyTypeOutput(UUID id, String name, String description, boolean active) {

    public static SurveyTypeOutput from(SurveyType surveyType) {
        return new SurveyTypeOutput(surveyType.getId(), surveyType.getName(),
            surveyType.getDescription(), surveyType.isActive());
    }
}
