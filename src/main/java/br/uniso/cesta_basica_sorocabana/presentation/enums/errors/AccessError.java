package br.uniso.cesta_basica_sorocabana.presentation.enums.errors;

public enum AccessError {
    PERMISSION_DENIED("Você não tem permissão para realizar esta ação."),
    INVALID_CREDENTIALS("E-mail ou senha inválidos."),
    ACCOUNT_DISABLED("Este usuário está desativado.");

    private final String template;

    AccessError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
