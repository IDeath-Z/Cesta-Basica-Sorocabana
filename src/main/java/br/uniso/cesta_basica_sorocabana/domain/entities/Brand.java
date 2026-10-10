package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.BrandError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Brand {

    private final UUID id;
    private final String name;
    private final boolean active;

    private Brand(UUID id, String name, boolean active) {
        this.id = DomainValidator.requireNonNull(id, BrandError.ID_REQUIRED.message());
        this.name = DomainValidator.requireNonBlank(name, BrandError.NAME_REQUIRED.message());
        this.active = active;
    }

    public static Brand createNew(String name) {
        return new Brand(UUID.randomUUID(), name, true);
    }

    public static Brand restore(UUID id, String name, boolean active) {
        return new Brand(id, name, active);
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

    public Brand rename(String name) {
        return new Brand(this.id, name, this.active);
    }

    public Brand activate() {
        if (this.active)
            return this;

        return new Brand(this.id, this.name, true);
    }

    public Brand deactivate() {
        if (!this.active)
            return this;

        return new Brand(this.id, this.name, false);
    }
}
