package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrandStore;

public interface ProductBrandStoreRepository {

    ProductBrandStore save(ProductBrandStore productBrandStore);
    Optional<ProductBrandStore> findById(UUID id);
    List<ProductBrandStore> findByProductBrandId(UUID productBrandId);
    boolean existsByProductBrandIdAndStoreId(UUID productBrandId, UUID storeId);
}
