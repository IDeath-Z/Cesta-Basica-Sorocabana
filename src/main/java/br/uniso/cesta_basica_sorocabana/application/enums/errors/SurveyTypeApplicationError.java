package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum SurveyTypeApplicationError {
    NOT_FOUND("Tipo de coleta %s não encontrado."),
    INACTIVE("O tipo de coleta %s está inativo."),
    NAME_ALREADY_EXISTS("Já existe um tipo de coleta com o nome %s.");

    private final String template;

    SurveyTypeApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
