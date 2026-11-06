package br.uniso.cesta_basica_sorocabana.application.enums.errors;

public enum ProductBrandApplicationError {
    NOT_FOUND("Vínculo de marca com produto %s não encontrado."),
    INACTIVE("O vínculo de marca com produto %s está inativo."),
    ALREADY_EXISTS("A marca já está vinculada a este produto.");

    private final String template;

    ProductBrandApplicationError(String template) {
        this.template = template;
    }

    public String message(Object... args) {
        return template.formatted(args);
    }
}
