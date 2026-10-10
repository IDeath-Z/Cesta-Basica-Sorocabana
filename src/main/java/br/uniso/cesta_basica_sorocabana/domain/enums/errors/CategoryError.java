package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum CategoryError {
    ID_REQUIRED("O ID da categoria é obrigatório."),
    NAME_REQUIRED("O nome da categoria é obrigatório.");

    private final String template;

    CategoryError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
