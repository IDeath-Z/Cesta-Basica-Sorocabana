package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Store;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface StoreRepository {

    Store save(Store store);
    Optional<Store> findById(UUID id);
    DomainPageResult<Store> findAll(DomainPageRequest pageRequest);
    boolean existsByName(String name);
}
