package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.domain.ports.BasketQuery;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchSurveyUseCase {

    private final SurveyRepository surveyRepository;
    private final BasketQuery basketQuery;

    public record Input(UUID id) {}

    public SurveyDetailOutput execute(Input input) {
        Survey survey = surveyRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(SurveyApplicationError.NOT_FOUND.message(input.id())));

        return SurveyDetailOutput.from(survey, basketQuery.findSavedBySurveyId(survey.getId()));
    }
}
