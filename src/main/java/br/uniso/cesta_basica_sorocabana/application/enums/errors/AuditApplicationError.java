package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum AuditApplicationError {
    NOT_FOUND("Registro de auditoria %s não encontrado.");

    private final String template;

    AuditApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
