package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.audit;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.uniso.cesta_basica_sorocabana.domain.enums.AuditAction;
import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.user.UserJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "audit")
public class AuditJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_audit_user"))
    private UserJpaEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, updatable = false, columnDefinition = "TEXT")
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity", nullable = false, updatable = false, columnDefinition = "TEXT")
    private AuditedEntity entity;

    @Column(name = "entity_id", nullable = false, updatable = false)
    private UUID entityId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_before", updatable = false, columnDefinition = "jsonb")
    private String dataBefore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_after", updatable = false, columnDefinition = "jsonb")
    private String dataAfter;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
