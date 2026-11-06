package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum ProductBrandStoreApplicationError {
    NOT_FOUND("Vínculo do item com o mercado %s não encontrado."),
    ALREADY_EXISTS("O item já está vinculado a este mercado.");

    private final String template;

    ProductBrandStoreApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
