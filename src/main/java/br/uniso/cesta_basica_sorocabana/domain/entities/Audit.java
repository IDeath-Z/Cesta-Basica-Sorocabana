package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.time.Instant;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.AuditAction;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.domain.enums.errors.AuditError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

// dataBefore and dataAfter are JSON strings. dataBefore is null only on INSERT, dataAfter is null only on DELETE.
public final class Audit {

    private final UUID id;
    private final UUID userId;
    private final AuditAction action;
    private final AuditedEntity entity;
    private final UUID entityId;
    private final String dataBefore;
    private final String dataAfter;
    private final Instant createdAt;

    private Audit(UUID id, UUID userId, AuditAction action, AuditedEntity entity, UUID entityId, String dataBefore,
        String dataAfter, Instant createdAt) {
        this.id = DomainValidator.requireNonNull(id, AuditError.ID_REQUIRED.message());
        this.userId = DomainValidator.requireNonNull(userId, AuditError.USER_ID_REQUIRED.message());
        this.action = DomainValidator.requireNonNull(action, AuditError.ACTION_REQUIRED.message());
        this.entity = DomainValidator.requireNonNull(entity, AuditError.ENTITY_REQUIRED.message());
        this.entityId = DomainValidator.requireNonNull(entityId, AuditError.ENTITY_ID_REQUIRED.message());
        this.dataBefore = dataBefore;
        this.dataAfter = dataAfter;
        this.createdAt = DomainValidator.requireNonNull(createdAt, AuditError.CREATION_DATE_REQUIRED.message());

        if ((action == AuditAction.INSERT) != (dataBefore == null)) {
            throw new IllegalArgumentException(AuditError.DATA_BEFORE_ONLY_NULL_ON_INSERT.message());
        }
        if ((action == AuditAction.DELETE) != (dataAfter == null)) {
            throw new IllegalArgumentException(AuditError.DATA_AFTER_ONLY_NULL_ON_DELETE.message());
        }
    }

    public static Audit createNew(UUID userId, AuditAction action, AuditedEntity entity, UUID entityId,
        String dataBefore, String dataAfter) {
        return new Audit(UUID.randomUUID(), userId, action, entity, entityId, dataBefore, dataAfter, Instant.now());
    }

    public static Audit restore(UUID id, UUID userId, AuditAction action, AuditedEntity entity, UUID entityId,
        String dataBefore, String dataAfter, Instant createdAt) {
        return new Audit(id, userId, action, entity, entityId, dataBefore, dataAfter, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public AuditAction getAction() {
        return action;
    }

    public AuditedEntity getEntity() {
        return entity;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public String getDataBefore() {
        return dataBefore;
    }

    public String getDataAfter() {
        return dataAfter;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
