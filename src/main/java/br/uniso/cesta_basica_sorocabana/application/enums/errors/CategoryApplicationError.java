package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum CategoryApplicationError {
    NOT_FOUND("Categoria %s não encontrada."),
    INACTIVE("A categoria %s está inativa."),
    NAME_ALREADY_EXISTS("Já existe uma categoria com o nome %s.");

    private final String template;

    CategoryApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
