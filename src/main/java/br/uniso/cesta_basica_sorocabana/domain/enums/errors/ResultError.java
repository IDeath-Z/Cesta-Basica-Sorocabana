package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum ResultError {
    SURVEY_ITEM_ID_REQUIRED("O ID do levantamento é obrigatório."),
    AVERAGE_PRICE_REQUIRED("O preço médio é obrigatório."),
    LOWEST_PRICE_REQUIRED("O menor preço é obrigatório."),
    HIGHEST_PRICE_REQUIRED("O maior preço é obrigatório."),
    LOWEST_PRICE_GREATER_THAN_HIGHEST("O menor preço não pode ser maior que o maior preço."),
    DIFFERENCE_AMOUNT_NEGATIVE("A diferença em valor não pode ser negativa."),
    DIFFERENCE_PERCENT_NEGATIVE("A diferença percentual não pode ser negativa.");

    private final String template;

    ResultError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
