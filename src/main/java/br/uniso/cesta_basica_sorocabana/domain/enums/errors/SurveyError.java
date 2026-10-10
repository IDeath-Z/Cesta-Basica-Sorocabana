package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum SurveyError {
    ID_REQUIRED("O ID da coleta é obrigatório."),
    SURVEY_DATE_REQUIRED("A data da coleta é obrigatória."),
    SURVEY_TYPE_ID_REQUIRED("O ID do tipo de coleta é obrigatório."),
    CREATION_DATE_REQUIRED("A data de criação é obrigatória.");

    private final String template;

    SurveyError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
