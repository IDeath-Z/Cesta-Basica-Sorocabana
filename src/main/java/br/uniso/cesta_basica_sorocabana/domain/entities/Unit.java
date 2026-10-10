package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.UnitError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class Unit {

    private final UUID id;
    private final BigDecimal quantity;
    private final String measure;
    private final boolean active;

    private Unit(UUID id, BigDecimal quantity, String measure, boolean active) {
        this.id = DomainValidator.requireNonNull(id, UnitError.ID_REQUIRED.message());
        this.quantity = DomainValidator.requirePositive(quantity, UnitError.QUANTITY_NOT_POSITIVE.message());
        this.measure = DomainValidator.requireNonBlank(measure, UnitError.MEASURE_REQUIRED.message());
        this.active = active;
    }

    public static Unit createNew(BigDecimal quantity, String measure) {
        return new Unit(UUID.randomUUID(), quantity, measure, true);
    }

    public static Unit restore(UUID id, BigDecimal quantity, String measure, boolean active) {
        return new Unit(id, quantity, measure, active);
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public boolean isActive() {
        return active;
    }

    public Unit update(BigDecimal quantity, String measure) {
        return new Unit(
            this.id,
            quantity != null ? quantity : this.quantity,
            measure != null ? measure : this.measure,
            this.active
        );
    }

    public Unit activate() {
        if (this.active)
            return this;

        return new Unit(this.id, this.quantity, this.measure, true);
    }

    public Unit deactivate() {
        if (!this.active)
            return this;

        return new Unit(this.id, this.quantity, this.measure, false);
    }
}
