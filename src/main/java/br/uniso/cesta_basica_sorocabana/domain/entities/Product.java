package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.ProductError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Product {

    private final UUID id;
    private final UUID categoryId;
    private final UUID unitId;
    private final String name;
    private final String specification;
    private final String notes;
    private final boolean active;

    private Product(UUID id, UUID categoryId, UUID unitId, String name, String specification, String notes,
        boolean active) {
        this.id = DomainValidator.requireNonNull(id, ProductError.ID_REQUIRED.message());
        this.categoryId = DomainValidator.requireNonNull(categoryId, ProductError.CATEGORY_ID_REQUIRED.message());
        this.unitId = unitId;
        this.name = DomainValidator.requireNonBlank(name, ProductError.NAME_REQUIRED.message());
        this.specification = specification;
        this.notes = notes;
        this.active = active;
    }

    public static Product createNew(UUID categoryId, UUID unitId, String name, String specification, String notes) {
        return new Product(UUID.randomUUID(), categoryId, unitId, name, specification, notes, true);
    }

    public static Product restore(UUID id, UUID categoryId, UUID unitId, String name, String specification,
        String notes, boolean active) {
        return new Product(id, categoryId, unitId, name, specification, notes, active);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public String getName() {
        return name;
    }

    public String getSpecification() {
        return specification;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public Product update(UUID categoryId, UUID unitId, String name, String specification, String notes) {
        return new Product(
            this.id,
            categoryId != null ? categoryId : this.categoryId,
            unitId != null ? unitId : this.unitId,
            name != null ? name : this.name,
            specification != null ? specification : this.specification,
            notes != null ? notes : this.notes,
            this.active
        );
    }

    public Product activate() {
        if (this.active)
            return this;

        return new Product(this.id, this.categoryId, this.unitId, this.name, this.specification, this.notes, true);
    }

    public Product deactivate() {
        if (!this.active)
            return this;

        return new Product(this.id, this.categoryId, this.unitId, this.name, this.specification, this.notes, false);
    }
}
