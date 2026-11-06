package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.BasketQuery;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemPriceRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyItemRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyRepository;

import lombok.RequiredArgsConstructor;

// Only a draft can be deleted. The audit keeps everything that was saved in it.
@RequiredArgsConstructor
public final class DeleteSurveyUseCase {

    private final SurveyRepository surveyRepository;
    private final SurveyItemRepository surveyItemRepository;
    private final SurveyItemPriceRepository surveyItemPriceRepository;
    private final BasketQuery basketQuery;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id) {}

    public void execute(Input input) {
        Survey survey = surveyRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(SurveyApplicationError.NOT_FOUND.message(input.id())));
        if (!survey.isDraft())
            throw new BusinessRuleException(SurveyApplicationError.NOT_DRAFT.message(survey.getId()));

        SurveyDetailOutput before = SurveyDetailOutput.from(survey, basketQuery.findSavedBySurveyId(survey.getId()));

        surveyItemPriceRepository.findBySurveyId(survey.getId())
            .forEach(price -> surveyItemPriceRepository.deleteById(price.getSurveyItemId(), price.getStoreId()));
        surveyItemRepository.findBySurveyId(survey.getId())
            .forEach(item -> surveyItemRepository.deleteById(item.getId()));
        surveyRepository.deleteById(survey.getId());

        auditRecorder.recordDelete(input.requesterId(), AuditedEntity.SURVEY, survey.getId(), before);
    }
}
