package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum ProductBrandError {
    ID_REQUIRED("O ID do produto + marca é obrigatório."),
    PRODUCT_ID_REQUIRED("O ID do produto é obrigatório."),
    BRAND_ID_REQUIRED("O ID da marca é obrigatório.");

    private final String template;

    ProductBrandError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
