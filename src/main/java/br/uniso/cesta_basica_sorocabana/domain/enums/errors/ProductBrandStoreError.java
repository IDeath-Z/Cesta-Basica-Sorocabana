package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum ProductBrandStoreError {
    ID_REQUIRED("O ID do vínculo do item com o mercado é obrigatório."),
    PRODUCT_BRAND_ID_REQUIRED("O ID do produto + marca é obrigatório."),
    STORE_ID_REQUIRED("O ID do mercado é obrigatório."),
    SEARCH_NAME_BLANK("O nome de pesquisa não pode ser vazio. Use nulo quando o mercado não tem o item.");

    private final String template;

    ProductBrandStoreError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
