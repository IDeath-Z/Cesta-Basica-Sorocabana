package br.uniso.cesta_basica_sorocabana.presentation.enums;

import java.util.Arrays;

import br.uniso.cesta_basica_sorocabana.domain.enums.AuditedEntity;

// Entity filter of GET /audits: ALL plus one constant per AuditedEntity, with the same names.
public enum AuditEntityFilter {
    ALL,
    USER,
    USER_PASSWORD,
    ROLE,
    BRAND,
    CATEGORY,
    STORE,
    UNIT,
    SURVEY_TYPE,
    PRODUCT,
    PRODUCT_BRAND;

    // Fails at startup if a new AuditedEntity was added without its filter here.
    static {
        Arrays.stream(AuditedEntity.values()).forEach(entity -> valueOf(entity.name()));
    }

    // ALL means no entity filter.
    public AuditedEntity toAuditedEntity() {
        return this == ALL ? null : AuditedEntity.valueOf(name());
    }
}
