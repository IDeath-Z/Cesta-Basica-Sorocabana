package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.StoreError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Store {

    private final UUID id;
    private final String name;
    private final String storeUrl;
    private final boolean active;

    private Store(UUID id, String name, String storeUrl, boolean active) {
        this.id = DomainValidator.requireNonNull(id, StoreError.ID_REQUIRED.message());
        this.name = DomainValidator.requireNonBlank(name, StoreError.NAME_REQUIRED.message());
        this.storeUrl = storeUrl;
        this.active = active;
    }

    public static Store createNew(String name, String storeUrl) {
        return new Store(UUID.randomUUID(), name, storeUrl, true);
    }

    public static Store restore(UUID id, String name, String storeUrl, boolean active) {
        return new Store(id, name, storeUrl, active);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public boolean isActive() {
        return active;
    }

    public Store update(String name, String storeUrl) {
        return new Store(
            this.id,
            name != null ? name : this.name,
            storeUrl != null ? storeUrl : this.storeUrl,
            this.active
        );
    }

    public Store activate() {
        if (this.active)
            return this;

        return new Store(this.id, this.name, this.storeUrl, true);
    }

    public Store deactivate() {
        if (!this.active)
            return this;

        return new Store(this.id, this.name, this.storeUrl, false);
    }
}
