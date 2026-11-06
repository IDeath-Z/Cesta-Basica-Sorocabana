package br.uniso.cesta_basica_sorocabana.application.usecases.surveytype;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyTypeApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateSurveyTypeUseCase {

    private final SurveyTypeRepository surveyTypeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, String name, String description) {}

    public SurveyTypeOutput execute(Input input) {
        if (surveyTypeRepository.existsByName(input.name()))
            throw new ResourceAlreadyExistsException(SurveyTypeApplicationError.NAME_ALREADY_EXISTS.message(input.name()));

        SurveyType surveyType = surveyTypeRepository.save(SurveyType.createNew(input.name(), input.description()));
        SurveyTypeOutput output = SurveyTypeOutput.from(surveyType);

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.SURVEY_TYPE, output.id(), output);
        return output;
    }
}
