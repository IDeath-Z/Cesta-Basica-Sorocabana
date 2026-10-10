package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum BrandError {
    ID_REQUIRED("O ID da marca é obrigatório."),
    NAME_REQUIRED("O nome da marca é obrigatório.");

    private final String template;

    BrandError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
