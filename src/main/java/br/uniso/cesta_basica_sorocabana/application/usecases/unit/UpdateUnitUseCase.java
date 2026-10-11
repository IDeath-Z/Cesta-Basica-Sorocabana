package br.uniso.cesta_basica_sorocabana.application.usecases.unit;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UnitApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateUnitUseCase {

    private final UnitRepository unitRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, UUID id, BigDecimal quantity, String measure) {}

    public UnitOutput execute(Input input) {
        Unit unit = unitRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException(UnitApplicationError.NOT_FOUND.message(input.id())));

        Unit updated = unit.update(input.quantity(), input.measure());
        boolean changed = updated.getQuantity().compareTo(unit.getQuantity()) != 0 || !updated.getMeasure().equals(unit.getMeasure());

        if (changed && unitRepository.existsByQuantityAndMeasure(updated.getQuantity(), updated.getMeasure()))
            throw new ResourceAlreadyExistsException(UnitApplicationError.ALREADY_EXISTS.message(updated.getQuantity(), updated.getMeasure()));

        UnitOutput before = UnitOutput.from(unit);
        UnitOutput after = UnitOutput.from(unitRepository.save(updated));

        auditRecorder.recordUpdate(input.requesterId(), AuditedEntity.UNIT, after.id(), before, after);
        return after;
    }
}
