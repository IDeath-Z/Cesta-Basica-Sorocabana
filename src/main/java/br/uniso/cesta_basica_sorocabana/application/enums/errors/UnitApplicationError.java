package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum UnitApplicationError {
    NOT_FOUND("Unidade %s não encontrada."),
    INACTIVE("A unidade %s está inativa."),
    ALREADY_EXISTS("Já existe uma unidade de %s %s.");

    private final String template;

    UnitApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
