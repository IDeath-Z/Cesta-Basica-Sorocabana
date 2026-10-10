package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum UserError {
    ID_REQUIRED("O ID do usuário é obrigatório."),
    EMAIL_REQUIRED("O e-mail é obrigatório."),
    PASSWORD_REQUIRED("A senha é obrigatória."),
    ROLE_ID_REQUIRED("O ID da função é obrigatório."),
    CREATION_DATE_REQUIRED("A data de criação é obrigatória.");

    private final String template;

    UserError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
