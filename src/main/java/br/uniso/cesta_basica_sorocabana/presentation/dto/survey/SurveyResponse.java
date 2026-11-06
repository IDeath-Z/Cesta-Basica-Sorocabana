package br.uniso.cesta_basica_sorocabana.presentation.dto.survey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.survey.SurveyOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SurveyResponse", description = "A price survey on a date")
public record SurveyResponse(
    @Schema(description = "Unique survey identifier", example = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d")
    UUID id,

    @Schema(description = "Survey date", example = "2026-11-10")
    LocalDate surveyDate,

    @Schema(description = "Survey type ID", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b")
    UUID surveyTypeId,

    @Schema(description = "Free notes", example = "Week of the holiday")
    String notes,

    @Schema(description = "True while the survey still accepts changes", example = "true")
    boolean draft,

    @Schema(description = "When the survey was opened", example = "2026-11-10T14:15:36.625Z")
    Instant createdAt
) {
    public static SurveyResponse from(SurveyOutput output) {
        return new SurveyResponse(output.id(), output.surveyDate(), output.surveyTypeId(), output.notes(), output.draft(), output.createdAt());
    }
}
