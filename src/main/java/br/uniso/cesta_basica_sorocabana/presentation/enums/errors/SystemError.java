package br.uniso.cesta_basica_sorocabana.presentation.enums.errors;

public enum SystemError {
    UNEXPECTED_ERROR("Ocorreu um erro inesperado. Tente novamente mais tarde.");

    private final String template;

    SystemError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
