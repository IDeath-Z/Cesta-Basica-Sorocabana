package br.uniso.cesta_basica_sorocabana.application.usecases.survey;

import java.time.LocalDate;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyApplicationError;
import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyTypeApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Survey;
import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateSurveyUseCase {

    private final SurveyRepository surveyRepository;
    private final SurveyTypeRepository surveyTypeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, LocalDate surveyDate, UUID surveyTypeId, String notes) {}

    public SurveyOutput execute(Input input) {
        Survey survey = surveyRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(SurveyApplicationError.NOT_FOUND.message(input.id())));
        if (!survey.isDraft())
            throw new BusinessRuleException(SurveyApplicationError.NOT_DRAFT.message(survey.getId()));

        if (input.surveyTypeId() != null && !input.surveyTypeId().equals(survey.getSurveyTypeId())) {
            SurveyType surveyType = surveyTypeRepository.findById(input.surveyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(SurveyTypeApplicationError.NOT_FOUND.message(input.surveyTypeId())));
            if (!surveyType.isActive())
                throw new BusinessRuleException(SurveyTypeApplicationError.INACTIVE.message(surveyType.getName()));
        }

        Survey updated = survey.update(input.surveyDate(), input.surveyTypeId(), input.notes());

        boolean identityChanged = !updated.getSurveyDate().equals(survey.getSurveyDate())
            || !updated.getSurveyTypeId().equals(survey.getSurveyTypeId());
        if (identityChanged && surveyRepository.existsBySurveyDateAndSurveyTypeId(updated.getSurveyDate(), updated.getSurveyTypeId()))
            throw new ResourceAlreadyExistsException(SurveyApplicationError.ALREADY_EXISTS.message(updated.getSurveyDate()));

        SurveyOutput before = SurveyOutput.from(survey);
        SurveyOutput after = SurveyOutput.from(surveyRepository.save(updated));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.SURVEY, after.id(), before, after);
        return after;
    }
}
