package br.uniso.cesta_basica_sorocabana.application.usecases.surveytype;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.SurveyTypeApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.SurveyType;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.SurveyTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateSurveyTypeStatusUseCase {

    private final SurveyTypeRepository surveyTypeRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public SurveyTypeOutput execute(Input input) {
        SurveyType surveyType = surveyTypeRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(SurveyTypeApplicationError.NOT_FOUND.message(input.id())));

        SurveyType changed = input.active() ? surveyType.activate() : surveyType.deactivate();
        if (changed == surveyType)
            return SurveyTypeOutput.from(surveyType);

        SurveyTypeOutput before = SurveyTypeOutput.from(surveyType);
        SurveyTypeOutput after = SurveyTypeOutput.from(surveyTypeRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.SURVEY_TYPE, after.id(), before, after);
        return after;
    }
}
