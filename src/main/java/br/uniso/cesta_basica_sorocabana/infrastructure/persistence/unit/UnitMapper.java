package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.unit;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;

@Component
public final class UnitMapper {

    public UnitJpaEntity toJpa(Unit unit) {
        UnitJpaEntity entity = new UnitJpaEntity();
        entity.setId(unit.getId());
        entity.setQuantity(unit.getQuantity());
        entity.setMeasure(unit.getMeasure());
        entity.setActive(unit.isActive());
        return entity;
    }

    public Unit toDomain(UnitJpaEntity entity) {
        return Unit.restore(
            entity.getId(),
            entity.getQuantity(),
            entity.getMeasure(),
            entity.isActive()
        );
    }
}
