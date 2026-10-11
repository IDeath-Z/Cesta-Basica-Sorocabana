package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum ProductApplicationError {
    NOT_FOUND("Produto %s não encontrado."),
    INACTIVE("O produto %s está inativo."),
    ALREADY_EXISTS("Já existe o produto %s com essa especificação.");

    private final String template;

    ProductApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
