package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum BrandApplicationError {
    NOT_FOUND("Marca %s não encontrada."),
    INACTIVE("A marca %s está inativa."),
    NAME_ALREADY_EXISTS("Já existe uma marca com o nome %s.");

    private final String template;

    BrandApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
