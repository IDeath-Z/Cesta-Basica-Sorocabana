package br.uniso.cesta_basica_sorocabana.application.usecases.surveytype;

import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchSurveyTypesUseCase {

    private final SurveyTypeRepository surveyTypeRepository;

    public record Input(DomainPageRequest pageRequest) {}

    public DomainPageResult<SurveyTypeOutput> execute(Input input) {
        return surveyTypeRepository.findAll(input.pageRequest()).map(SurveyTypeOutput::from);
    }
}
