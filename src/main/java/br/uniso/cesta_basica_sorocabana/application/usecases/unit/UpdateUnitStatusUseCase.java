package br.uniso.cesta_basica_sorocabana.application.usecases.unit;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UnitApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateUnitStatusUseCase {

    private final UnitRepository unitRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, boolean active) {}

    // Already in the requested state: nothing changes, so nothing is saved or audited.
    public UnitOutput execute(Input input) {
        Unit unit = unitRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(UnitApplicationError.NOT_FOUND.message(input.id())));

        Unit changed = input.active() ? unit.activate() : unit.deactivate();
        if (changed == unit)
            return UnitOutput.from(unit);

        UnitOutput before = UnitOutput.from(unit);
        UnitOutput after = UnitOutput.from(unitRepository.save(changed));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.UNIT, after.id(), before, after);
        return after;
    }
}
