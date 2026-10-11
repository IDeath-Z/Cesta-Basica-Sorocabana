package br.uniso.cesta_basica_sorocabana.application.usecases.store;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Store;

public record StoreOutput(UUID id, String name, boolean active) {

    public static StoreOutput from(Store store) {
        return new StoreOutput(store.getId(), store.getName(), store.isActive());
    }
}
