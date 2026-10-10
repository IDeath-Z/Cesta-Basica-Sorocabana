package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum RoleError {
    ID_REQUIRED("O ID da função é obrigatório."),
    NAME_REQUIRED("O nome da função é obrigatório.");

    private final String template;

    RoleError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
