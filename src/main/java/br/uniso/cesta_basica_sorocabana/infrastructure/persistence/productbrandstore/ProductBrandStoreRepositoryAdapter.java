package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrandstore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrandStore;
import br.uniso.cesta_basica_sorocabana.domain.ports.ProductBrandStoreRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaRepository;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.store.StoreJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.store.StoreJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class ProductBrandStoreRepositoryAdapter implements ProductBrandStoreRepository {

    private final ProductBrandStoreJpaRepository productBrandStoreJpaRepository;
    private final ProductBrandJpaRepository productBrandJpaRepository;
    private final StoreJpaRepository storeJpaRepository;
    private final ProductBrandStoreMapper productBrandStoreMapper;

    @Override
    public ProductBrandStore save(ProductBrandStore productBrandStore) {
        ProductBrandJpaEntity productBrandProxy = productBrandJpaRepository.getReferenceById(productBrandStore.getProductBrandId());
        StoreJpaEntity storeProxy = storeJpaRepository.getReferenceById(productBrandStore.getStoreId());

        ProductBrandStoreJpaEntity entity = productBrandStoreMapper.toJpa(productBrandStore, productBrandProxy, storeProxy);
        return productBrandStoreMapper.toDomain(productBrandStoreJpaRepository.save(entity));
    }

    @Override
    public Optional<ProductBrandStore> findById(UUID id) {
        return productBrandStoreJpaRepository.findById(id).map(productBrandStoreMapper::toDomain);
    }

    @Override
    public List<ProductBrandStore> findByProductBrandId(UUID productBrandId) {
        return productBrandStoreJpaRepository.findByProductBrandId(productBrandId)
            .stream()
            .map(productBrandStoreMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByProductBrandIdAndStoreId(UUID productBrandId, UUID storeId) {
        return productBrandStoreJpaRepository.existsByProductBrandIdAndStoreId(productBrandId, storeId);
    }
}
