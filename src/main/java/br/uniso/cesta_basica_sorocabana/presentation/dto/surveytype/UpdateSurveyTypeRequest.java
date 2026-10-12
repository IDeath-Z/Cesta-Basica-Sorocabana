package br.uniso.cesta_basica_sorocabana.presentation.dto.surveytype;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateSurveyTypeRequest", description = "Payload for updating a survey type. Omitted (null) fields keep their current value")
public record UpdateSurveyTypeRequest(
    @Size(min = 1, max = 40) @Schema(description = "Short unique code", example = "EASTER")
    String code,

    @Size(min = 1, max = 120) @Schema(description = "Display name", example = "Páscoa")
    String name,

    @Size(max = 500) @Schema(description = "Description", example = "Easter price survey")
    String description
) {}
