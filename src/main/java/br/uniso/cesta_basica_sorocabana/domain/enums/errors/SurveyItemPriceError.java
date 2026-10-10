package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum SurveyItemPriceError {
    SURVEY_ITEM_ID_REQUIRED("O ID do levantamento é obrigatório."),
    STORE_ID_REQUIRED("O ID do mercado é obrigatório."),
    PRICE_NOT_POSITIVE("O preço deve ser maior que zero.");

    private final String template;

    SurveyItemPriceError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
