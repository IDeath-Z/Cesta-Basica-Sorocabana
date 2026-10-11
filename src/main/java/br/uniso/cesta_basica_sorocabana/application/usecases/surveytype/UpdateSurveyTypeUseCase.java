package br.uniso.cesta_basica_sorocabana.application.usecases.surveytype;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyTypeApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateSurveyTypeUseCase {

    private final SurveyTypeRepository surveyTypeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, String code, String name, String description) {}

    public SurveyTypeOutput execute(Input input) {
        SurveyType surveyType = surveyTypeRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(SurveyTypeApplicationError.NOT_FOUND.message(input.id())));

        if (input.code() != null && !input.code().equals(surveyType.getCode()) && surveyTypeRepository.existsByCode(input.code()))
            throw new ResourceAlreadyExistsException(SurveyTypeApplicationError.CODE_ALREADY_EXISTS.message(input.code()));

        SurveyType updated = surveyType.update(input.code(), input.name(), input.description());
        SurveyTypeOutput before = SurveyTypeOutput.from(surveyType);
        SurveyTypeOutput after = SurveyTypeOutput.from(surveyTypeRepository.save(updated));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.SURVEY_TYPE, after.id(), before, after);
        return after;
    }
}
