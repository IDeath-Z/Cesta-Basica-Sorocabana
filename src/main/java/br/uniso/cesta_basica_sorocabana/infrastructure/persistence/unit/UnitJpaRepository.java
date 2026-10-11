package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.unit;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitJpaRepository extends JpaRepository<UnitJpaEntity, UUID> {

    boolean existsByQuantityAndMeasure(BigDecimal quantity, String measure);
}
