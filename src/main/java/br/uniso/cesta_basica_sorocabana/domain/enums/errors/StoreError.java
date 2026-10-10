package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum StoreError {
    ID_REQUIRED("O ID do mercado é obrigatório."),
    NAME_REQUIRED("O nome do mercado é obrigatório.");

    private final String template;

    StoreError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
