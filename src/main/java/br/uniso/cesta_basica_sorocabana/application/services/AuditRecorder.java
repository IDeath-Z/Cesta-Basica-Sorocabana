package br.uniso.cesta_basica_sorocabana.application.services;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Audit;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditAction;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.ports.AuditRepository;
import br.uniso.cesta_basica_sorocabana.domain.ports.DataSerializer;

import lombok.RequiredArgsConstructor;

// Saves who changed what, with the record before and after the change as JSON.
// Called by every use case that changes data, inside the same transaction, so the change and its audit are atomic.
@RequiredArgsConstructor
public final class AuditRecorder {

    private final AuditRepository auditRepository;
    private final DataSerializer dataSerializer;

    public void recordInsert(UUID requesterId, AuditedEntity entity, UUID entityId, Object after) {
        auditRepository.save(Audit.createNew(requesterId, AuditAction.INSERT, entity, entityId, null, dataSerializer.toJson(after)));
    }

    public void recordUpdate(UUID requesterId, AuditedEntity entity, UUID entityId, Object before, Object after) {
        auditRepository.save(Audit.createNew(requesterId, AuditAction.UPDATE, entity, entityId,
            dataSerializer.toJson(before), dataSerializer.toJson(after)));
    }
}
