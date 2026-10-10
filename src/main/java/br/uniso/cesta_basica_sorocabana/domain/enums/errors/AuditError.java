package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum AuditError {
    ID_REQUIRED("O ID do registro de auditoria é obrigatório."),
    USER_ID_REQUIRED("O ID do usuário é obrigatório."),
    ACTION_REQUIRED("A ação é obrigatória."),
    INVALID_ACTION("Ação de auditoria %s inválida."),
    ENTITY_REQUIRED("A entidade é obrigatória."),
    ENTITY_ID_REQUIRED("O ID da entidade é obrigatório."),
    CREATION_DATE_REQUIRED("A data de criação é obrigatória."),
    DATA_BEFORE_ONLY_NULL_ON_INSERT("O estado anterior deve ser nulo apenas em INSERT."),
    DATA_AFTER_ONLY_NULL_ON_DELETE("O estado posterior deve ser nulo apenas em DELETE.");

    private final String template;

    AuditError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
