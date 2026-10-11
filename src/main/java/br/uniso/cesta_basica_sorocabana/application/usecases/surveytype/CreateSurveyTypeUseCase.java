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

    public record Input(UUID requesterId, String code, String name, String description) {}

    public SurveyTypeOutput execute(Input input) {
        if (surveyTypeRepository.existsByCode(input.code()))
            throw new ResourceAlreadyExistsException(SurveyTypeApplicationError.CODE_ALREADY_EXISTS.message(input.code()));

        SurveyType surveyType = surveyTypeRepository.save(SurveyType.createNew(input.code(), input.name(), input.description()));
        SurveyTypeOutput output = SurveyTypeOutput.from(surveyType);

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.SURVEY_TYPE, output.id(), output);
        return output;
    }
}
