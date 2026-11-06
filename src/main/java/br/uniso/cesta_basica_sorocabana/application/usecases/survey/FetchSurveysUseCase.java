package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchSurveysUseCase {

    private final SurveyRepository surveyRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<SurveyOutput> execute(Input input) {
        return surveyRepository.findAll(input.pageRequest()).map(SurveyOutput::from);
    }
}
