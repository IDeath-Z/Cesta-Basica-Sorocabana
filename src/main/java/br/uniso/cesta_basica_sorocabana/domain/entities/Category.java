package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.CategoryError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Category {

    private final UUID id;
    private final String name;
    private final boolean active;

    private Category(UUID id, String name, boolean active) {
        this.id = DomainValidator.requireNonNull(id, CategoryError.ID_REQUIRED.message());
        this.name = DomainValidator.requireNonBlank(name, CategoryError.NAME_REQUIRED.message());
        this.active = active;
    }

    public static Category createNew(String name) {
        return new Category(UUID.randomUUID(), name, true);
    }

    public static Category restore(UUID id, String name, boolean active) {
        return new Category(id, name, active);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Category rename(String name) {
        return new Category(this.id, name, this.active);
    }

    public Category activate() {
        if (this.active)
            return this;

        return new Category(this.id, this.name, true);
    }

    public Category deactivate() {
        if (!this.active)
            return this;

        return new Category(this.id, this.name, false);
    }
}
