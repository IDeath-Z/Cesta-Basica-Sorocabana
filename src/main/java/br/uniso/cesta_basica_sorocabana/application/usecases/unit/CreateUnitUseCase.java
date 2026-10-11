package br.uniso.cesta_basica_sorocabana.application.usecases.unit;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.enums.errors.UnitApplicationError;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.services.AuditRecorder;
import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateUnitUseCase {

    private final UnitRepository unitRepository;
    private final AuditRecorder auditRecorder;

    public record Input(UUID requesterId, BigDecimal quantity, String measure) {}

    public UnitOutput execute(Input input) {
        if (unitRepository.existsByQuantityAndMeasure(input.quantity(), input.measure()))
            throw new ResourceAlreadyExistsException(UnitApplicationError.ALREADY_EXISTS.message(input.quantity(), input.measure()));

        UnitOutput output = UnitOutput.from(unitRepository.save(Unit.createNew(input.quantity(), input.measure())));

        auditRecorder.recordInsert(input.requesterId(), AuditedEntity.UNIT, output.id(), output);
        return output;
    }
}
