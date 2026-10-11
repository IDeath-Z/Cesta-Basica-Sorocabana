package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum StoreApplicationError {
    NOT_FOUND("Mercado %s não encontrado."),
    NAME_ALREADY_EXISTS("Já existe um mercado com o nome %s.");

    private final String template;

    StoreApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
