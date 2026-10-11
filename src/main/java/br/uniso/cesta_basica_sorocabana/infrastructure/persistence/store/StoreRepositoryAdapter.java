package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.store;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Store;
import br.uniso.cesta_basica_sorocabana.domain.ports.StoreRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class StoreRepositoryAdapter implements StoreRepository {

    private final StoreJpaRepository storeJpaRepository;
    private final StoreMapper storeMapper;

    @Override
    public Store save(Store store) {
        StoreJpaEntity entity = storeMapper.toJpa(store);
        return storeMapper.toDomain(storeJpaRepository.save(entity));
    }

    @Override
    public Optional<Store> findById(UUID id) {
        return storeJpaRepository.findById(id).map(storeMapper::toDomain);
    }

    @Override
    public DomainPageResult<Store> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<StoreJpaEntity> page = storeJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, storeMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return storeJpaRepository.existsByName(name);
    }
}
