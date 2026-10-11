package br.uniso.cesta_basica_sorocabana.infrastructure.enums.errors;

public enum AuthError {
    ACCESS_DENIED("Acesso negado. Faça login ou informe um token válido."),
    USER_NOT_FOUND("Usuário %s não encontrado."),
    INVALID_ACCESS_TOKEN("Token de acesso inválido ou expirado."),
    TOKEN_GENERATION_FAILED("Erro ao gerar o token de acesso."),
    UNEXPECTED_PRINCIPAL("Tipo de principal inesperado: %s.");

    private final String template;

    AuthError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
