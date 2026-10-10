package br.uniso.cesta_basica_sorocabana.domain.enums;

// What an audit record refers to. USER_PASSWORD is separate from USER so the history never holds password data.
public enum AuditedEntity {
    USER,
    USER_PASSWORD,
    ROLE,
    BRAND,
    CATEGORY,
    STORE,
    UNIT,
    SURVEY_TYPE,
    PRODUCT,
    PRODUCT_BRAND
}
