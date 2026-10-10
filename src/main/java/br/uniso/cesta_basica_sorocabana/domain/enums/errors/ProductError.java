package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum ProductError {
    ID_REQUIRED("O ID do produto é obrigatório."),
    CATEGORY_ID_REQUIRED("O ID da categoria é obrigatório."),
    NAME_REQUIRED("O nome do produto é obrigatório.");

    private final String template;

    ProductError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
