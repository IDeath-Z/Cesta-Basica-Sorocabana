package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum SurveyApplicationError {
    NOT_FOUND("Coleta %s não encontrada."),
    ALREADY_EXISTS("Já existe uma coleta deste tipo na data %s."),
    NOT_DRAFT("A coleta %s não é mais rascunho e não aceita alterações."),
    ITEM_NOT_ACTIVE("O item %s não está ativo no mercado %s."),
    DUPLICATE_ITEM("O item %s foi enviado mais de uma vez para o mercado %s.");

    private final String template;

    SurveyApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
