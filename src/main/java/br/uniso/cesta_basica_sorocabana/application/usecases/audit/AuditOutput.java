package br.uniso.cesta_basica_sorocabana.application.usecases.audit;

import java.time.Instant;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Audit;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditAction;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;

public record AuditOutput(UUID id, UUID userId, AuditAction action, AuditedEntity entity, UUID entityId, String dataBefore,
    String dataAfter, Instant createdAt) {

    public static AuditOutput from(Audit audit) {
        return new AuditOutput(audit.getId(), audit.getUserId(), audit.getAction(), audit.getEntity(), audit.getEntityId(),
            audit.getDataBefore(), audit.getDataAfter(), audit.getCreatedAt());
    }
}
