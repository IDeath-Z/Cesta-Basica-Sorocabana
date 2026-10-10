package br.uniso.cesta_basica_sorocabana.domain.shared;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;

// Optional, combinable audit filters: a null field means "any".
public record AuditFilter(
    UUID userId,
    AuditedEntity entity,
    UUID entityId
) {}
