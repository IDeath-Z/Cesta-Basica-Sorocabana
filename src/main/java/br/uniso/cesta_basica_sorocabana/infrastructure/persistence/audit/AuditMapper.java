package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.audit;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Audit;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.user.UserJpaEntity;

@Component
public final class AuditMapper {

    public AuditJpaEntity toJpa(Audit audit, UserJpaEntity userJpaEntity) {
        AuditJpaEntity entity = new AuditJpaEntity();
        entity.setId(audit.getId());
        entity.setUser(userJpaEntity);
        entity.setAction(audit.getAction());
        entity.setEntity(audit.getEntity());
        entity.setEntityId(audit.getEntityId());
        entity.setDataBefore(audit.getDataBefore());
        entity.setDataAfter(audit.getDataAfter());
        entity.setCreatedAt(audit.getCreatedAt());
        return entity;
    }

    public Audit toDomain(AuditJpaEntity entity) {
        return Audit.restore(
            entity.getId(),
            entity.getUser().getId(),
            entity.getAction(),
            entity.getEntity(),
            entity.getEntityId(),
            entity.getDataBefore(),
            entity.getDataAfter(),
            entity.getCreatedAt()
        );
    }
}
