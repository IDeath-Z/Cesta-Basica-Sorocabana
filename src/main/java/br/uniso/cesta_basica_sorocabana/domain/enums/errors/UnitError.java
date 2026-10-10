package br.uniso.cesta_basica_sorocabana.domain.enums.errors;

public enum UnitError {
    ID_REQUIRED("O ID da unidade é obrigatório."),
    QUANTITY_NOT_POSITIVE("A quantidade deve ser maior que zero."),
    MEASURE_REQUIRED("A grandeza é obrigatória.");

    private final String template;

    UnitError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
