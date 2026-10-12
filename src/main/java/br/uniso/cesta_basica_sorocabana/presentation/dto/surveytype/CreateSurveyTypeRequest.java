package br.uniso.cesta_basica_sorocabana.presentation.dto.surveytype;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateSurveyTypeRequest", description = "Payload for creating a survey type")
public record CreateSurveyTypeRequest(
    @NotBlank @Size(max = 40) @Schema(description = "Short unique code", example = "WEEKLY")
    String code,

    @NotBlank @Size(max = 120) @Schema(description = "Display name", example = "Semanal")
    String name,

    @Size(max = 500) @Schema(description = "Optional description", example = "Weekly price survey of the basic food basket")
    String description
) {}
