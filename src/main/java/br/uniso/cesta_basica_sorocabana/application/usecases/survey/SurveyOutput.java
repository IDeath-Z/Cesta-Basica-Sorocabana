package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;

public record SurveyOutput(UUID id, LocalDate surveyDate, UUID surveyTypeId, String notes, boolean draft, Instant createdAt) {

    public static SurveyOutput from(Survey survey) {
        return new SurveyOutput(survey.getId(), survey.getSurveyDate(), survey.getSurveyTypeId(), survey.getNotes(),
            survey.isDraft(), survey.getCreatedAt());
    }
}
