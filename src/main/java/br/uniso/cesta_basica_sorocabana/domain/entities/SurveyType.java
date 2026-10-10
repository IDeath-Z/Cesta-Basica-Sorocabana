package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.SurveyTypeError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class SurveyType {

    private final UUID id;
    private final String code;
    private final String name;
    private final String description;
    private final boolean active;

    private SurveyType(UUID id, String code, String name, String description, boolean active) {
        this.id = DomainValidator.requireNonNull(id, SurveyTypeError.ID_REQUIRED.message());
        this.code = DomainValidator.requireNonBlank(code, SurveyTypeError.CODE_REQUIRED.message());
        this.name = DomainValidator.requireNonBlank(name, SurveyTypeError.NAME_REQUIRED.message());
        this.description = description;
        this.active = active;
    }

    public static SurveyType createNew(String code, String name, String description) {
        return new SurveyType(UUID.randomUUID(), code, name, description, true);
    }

    public static SurveyType restore(UUID id, String code, String name, String description, boolean active) {
        return new SurveyType(id, code, name, description, active);
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public SurveyType update(String code, String name, String description) {
        return new SurveyType(
            this.id,
            code != null ? code : this.code,
            name != null ? name : this.name,
            description != null ? description : this.description,
            this.active
        );
    }

    public SurveyType activate() {
        if (this.active)
            return this;

        return new SurveyType(this.id, this.code, this.name, this.description, true);
    }

    public SurveyType deactivate() {
        if (!this.active)
            return this;

        return new SurveyType(this.id, this.code, this.name, this.description, false);
    }
}
