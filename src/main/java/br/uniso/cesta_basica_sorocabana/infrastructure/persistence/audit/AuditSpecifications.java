package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.audit;

import org.springframework.data.jpa.domain.Specification;

import br.uniso.cesta_basica_sorocabana.domain.shared.AuditFilter;

// Builds the WHERE clause from the filters that were given; missing filters add no condition.
public final class AuditSpecifications {

    private AuditSpecifications() {}

    public static Specification<AuditJpaEntity> from(AuditFilter filter) {
        Specification<AuditJpaEntity> spec = Specification.unrestricted();

        if (filter.userId() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), filter.userId()));
        if (filter.entity() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("entity"), filter.entity()));
        if (filter.entityId() != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("entityId"), filter.entityId()));

        return spec;
    }
}
