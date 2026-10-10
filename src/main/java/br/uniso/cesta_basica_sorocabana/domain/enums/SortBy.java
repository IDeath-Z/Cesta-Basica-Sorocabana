package br.uniso.cesta_basica_sorocabana.domain.enums;

public enum SortBy {
    NAME_ASC("name", SortDirection.ASC),
    NAME_DESC("name", SortDirection.DESC),
    EMAIL_ASC("email", SortDirection.ASC),
    EMAIL_DESC("email", SortDirection.DESC),
    MEASURE_ASC("measure", SortDirection.ASC),
    MEASURE_DESC("measure", SortDirection.DESC),
    CREATED_AT_ASC("createdAt", SortDirection.ASC),
    CREATED_AT_DESC("createdAt", SortDirection.DESC),
    SURVEY_DATE_ASC("surveyDate", SortDirection.ASC),
    SURVEY_DATE_DESC("surveyDate", SortDirection.DESC);

    private final String field;
    private final SortDirection direction;

    SortBy(String field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() { return field; }
    public SortDirection getDirection() { return direction; }
}
