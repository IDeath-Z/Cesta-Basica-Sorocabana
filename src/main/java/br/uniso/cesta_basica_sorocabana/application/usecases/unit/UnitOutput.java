package br.uniso.cesta_basica_sorocabana.application.usecases.unit;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;

public record UnitOutput(UUID id, BigDecimal quantity, String measure, boolean active) {

    public static UnitOutput from(Unit unit) {
        return new UnitOutput(unit.getId(), unit.getQuantity(), unit.getMeasure(), unit.isActive());
    }
}
