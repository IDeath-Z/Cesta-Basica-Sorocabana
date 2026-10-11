package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.audit;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuditJpaRepository extends JpaRepository<AuditJpaEntity, UUID>, JpaSpecificationExecutor<AuditJpaEntity> {
}
