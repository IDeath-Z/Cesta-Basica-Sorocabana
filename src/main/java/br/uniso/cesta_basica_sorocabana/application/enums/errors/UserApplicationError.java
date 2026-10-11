package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum UserApplicationError {
    NOT_FOUND("Usuário %s não encontrado."),
    EMAIL_ALREADY_REGISTERED("O e-mail %s já está cadastrado."),
    WRONG_CURRENT_PASSWORD("A senha atual está incorreta."),
    CANNOT_DEACTIVATE_SELF("Você não pode desativar o próprio usuário.");

    private final String template;

    UserApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
