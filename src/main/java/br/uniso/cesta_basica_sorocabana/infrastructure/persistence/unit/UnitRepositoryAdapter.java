package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.unit;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Unit;
import br.uniso.cesta_basica_sorocabana.domain.ports.UnitRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class UnitRepositoryAdapter implements UnitRepository {

    private final UnitJpaRepository unitJpaRepository;
    private final UnitMapper unitMapper;

    @Override
    public Unit save(Unit unit) {
        UnitJpaEntity entity = unitMapper.toJpa(unit);
        return unitMapper.toDomain(unitJpaRepository.save(entity));
    }

    @Override
    public Optional<Unit> findById(UUID id) {
        return unitJpaRepository.findById(id).map(unitMapper::toDomain);
    }

    @Override
    public DomainPageResult<Unit> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<UnitJpaEntity> page = unitJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, unitMapper::toDomain);
    }

    @Override
    public boolean existsByQuantityAndMeasure(BigDecimal quantity, String measure) {
        return unitJpaRepository.existsByQuantityAndMeasure(quantity, measure);
    }
}
