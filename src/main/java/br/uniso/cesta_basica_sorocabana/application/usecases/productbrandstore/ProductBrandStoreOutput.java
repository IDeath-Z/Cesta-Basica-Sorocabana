package br.uniso.cesta_basica_sorocabana.application.usecases.productbrandstore;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrandStore;

public record ProductBrandStoreOutput(UUID id, UUID productBrandId, UUID storeId, String searchName, boolean active) {

    public static ProductBrandStoreOutput from(ProductBrandStore productBrandStore) {
        return new ProductBrandStoreOutput(productBrandStore.getId(), productBrandStore.getProductBrandId(),
            productBrandStore.getStoreId(), productBrandStore.getSearchName(), productBrandStore.isActive());
    }
}
