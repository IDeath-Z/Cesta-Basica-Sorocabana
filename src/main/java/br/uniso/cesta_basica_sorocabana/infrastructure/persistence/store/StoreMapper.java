package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.store;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Store;

@Component
public final class StoreMapper {

    public StoreJpaEntity toJpa(Store store) {
        StoreJpaEntity entity = new StoreJpaEntity();
        entity.setId(store.getId());
        entity.setName(store.getName());
        entity.setActive(store.isActive());
        return entity;
    }

    public Store toDomain(StoreJpaEntity entity) {
        return Store.restore(
            entity.getId(),
            entity.getName(),
            entity.isActive()
        );
    }
}
