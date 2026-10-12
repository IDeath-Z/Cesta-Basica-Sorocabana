package br.uniso.cesta_basica_sorocabana.presentation.enums.errors;

public enum RequestError {
    VALIDATION_FAILED("Dados inválidos: %s"),
    INVALID_BODY("O corpo da requisição está ausente ou mal formatado."),
    INVALID_PARAMETER("O parâmetro %s tem um valor inválido."),
    MISSING_PARAMETER("O parâmetro %s é obrigatório."),
    INVALID_SORT("A ordenação escolhida não é válida para esta rota."),
    ROUTE_NOT_FOUND("Rota não encontrada."),
    METHOD_NOT_ALLOWED("Método %s não suportado nesta rota."),
    DATA_CONFLICT("A operação viola uma restrição dos dados cadastrados.");

    private final String template;

    RequestError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
