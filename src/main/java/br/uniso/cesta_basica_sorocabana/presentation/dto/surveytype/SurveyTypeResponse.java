package br.uniso.cesta_basica_sorocabana.presentation.dto.surveytype;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.surveytype.SurveyTypeOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SurveyTypeResponse", description = "Kind of price survey, e.g. weekly, Easter, Christmas")
public record SurveyTypeResponse(
    @Schema(description = "Unique survey type identifier", example = "c2e8f6a1-4b3d-4e7f-9a0b-5d6c7e8f9a1b")
    UUID id,

    @Schema(description = "Short unique code", example = "WEEKLY")
    String code,

    @Schema(description = "Display name", example = "Semanal")
    String name,

    @Schema(description = "Description", example = "Weekly price survey of the basic food basket")
    String description,

    @Schema(description = "Whether new surveys can use this type", example = "true")
    boolean active
) {
    public static SurveyTypeResponse from(SurveyTypeOutput output) {
        return new SurveyTypeResponse(output.id(), output.code(), output.name(), output.description(), output.active());
    }
}
