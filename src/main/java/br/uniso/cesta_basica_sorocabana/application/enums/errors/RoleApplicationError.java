package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum RoleApplicationError {
    NOT_FOUND("Função %s não encontrada."),
    INACTIVE("A função %s está inativa."),
    NAME_ALREADY_EXISTS("Já existe uma função com o nome %s."),
    PROTECTED("A função %s tem todas as permissões e não pode ser alterada nem desativada.");

    private final String template;

    RoleApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
