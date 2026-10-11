package br.uniso.cesta_basica_sorocabana.application.usecases.surveytype;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyTypeApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchSurveyTypeUseCase {

    private final SurveyTypeRepository surveyTypeRepository;

    public record Input(UUID id) {}

    public SurveyTypeOutput execute(Input input) {
        return surveyTypeRepository.findById(input.id())
            .map(SurveyTypeOutput::from)
            .orElseThrow(() -> new ResourceNotFoundException(SurveyTypeApplicationError.NOT_FOUND.message(input.id())));
    }
}
