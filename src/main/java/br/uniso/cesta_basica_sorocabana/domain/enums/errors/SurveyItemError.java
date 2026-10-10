package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum SurveyItemError {
    ID_REQUIRED("O ID do levantamento é obrigatório."),
    SURVEY_ID_REQUIRED("O ID da coleta é obrigatório."),
    PRODUCT_BRAND_ID_REQUIRED("O ID do produto + marca é obrigatório.");

    private final String template;

    SurveyItemError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
