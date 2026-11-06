package br.uniso.cesta_basica_sorocabana.presentation.dto.survey;

import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateSurveyRequest", description = "Payload for updating a draft survey. Omitted (null) fields keep their current value")
public record UpdateSurveyRequest(
    @Schema(description = "Survey date", example = "2026-11-11")
    LocalDate surveyDate,

    @Schema(description = "ID of an active survey type", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b")
    UUID surveyTypeId,

    @Size(max = 500) @Schema(description = "Free notes", example = "Week of the holiday")
    String notes
) {}
