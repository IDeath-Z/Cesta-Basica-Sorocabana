package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.StoreError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Store {

    private final UUID id;
    private final String name;
    private final boolean active;

    private Store(UUID id, String name, boolean active) {
        this.id = DomainValidator.requireNonNull(id, StoreError.ID_REQUIRED.message());
        this.name = DomainValidator.requireNonBlank(name, StoreError.NAME_REQUIRED.message());
        this.active = active;
    }

    public static Store createNew(String name) {
        return new Store(UUID.randomUUID(), name, true);
    }

    public static Store restore(UUID id, String name, boolean active) {
        return new Store(id, name, active);
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

    public Store rename(String name) {
        return new Store(this.id, name, this.active);
    }

    public Store activate() {
        if (this.active)
            return this;

        return new Store(this.id, this.name, true);
    }

    public Store deactivate() {
        if (!this.active)
            return this;

        return new Store(this.id, this.name, false);
    }
}
