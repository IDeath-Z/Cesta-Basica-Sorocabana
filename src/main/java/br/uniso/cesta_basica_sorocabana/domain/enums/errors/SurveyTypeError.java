package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum SurveyTypeError {
    ID_REQUIRED("O ID do tipo de coleta é obrigatório."),
    NAME_REQUIRED("O nome do tipo de coleta é obrigatório.");

    private final String template;

    SurveyTypeError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
