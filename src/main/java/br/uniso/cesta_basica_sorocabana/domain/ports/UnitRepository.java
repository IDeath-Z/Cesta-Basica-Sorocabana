package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface UnitRepository {

    Unit save(Unit unit);
    Optional<Unit> findById(UUID id);
    DomainPageResult<Unit> findAll(DomainPageRequest pageRequest);
    boolean existsByQuantityAndMeasure(BigDecimal quantity, String measure);
}
