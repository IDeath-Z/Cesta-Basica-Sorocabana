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
public final class CreateSurveyUseCase {

    private final SurveyRepository surveyRepository;
    private final SurveyTypeRepository surveyTypeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, LocalDate surveyDate, UUID surveyTypeId, String notes) {}

    public SurveyOutput execute(Input input) {
        SurveyType surveyType = surveyTypeRepository.findById(input.surveyTypeId())
            .orElseThrow(() -> new ResourceNotFoundException(SurveyTypeApplicationError.NOT_FOUND.message(input.surveyTypeId())));
        if (!surveyType.isActive())
            throw new BusinessRuleException(SurveyTypeApplicationError.INACTIVE.message(surveyType.getName()));

        if (surveyRepository.existsBySurveyDateAndSurveyTypeId(input.surveyDate(), input.surveyTypeId()))
            throw new ResourceAlreadyExistsException(SurveyApplicationError.ALREADY_EXISTS.message(input.surveyDate()));

        SurveyOutput output = SurveyOutput.from(surveyRepository.save(Survey.createNew(input.surveyDate(), input.surveyTypeId(), input.notes())));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.SURVEY, output.id(), output);
        return output;
    }
}
