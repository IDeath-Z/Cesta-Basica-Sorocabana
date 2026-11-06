package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrandstore;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrandStore;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand.ProductBrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.store.StoreJpaEntity;

@Component
public final class ProductBrandStoreMapper {

    public ProductBrandStoreJpaEntity toJpa(ProductBrandStore productBrandStore, ProductBrandJpaEntity productBrandJpaEntity, StoreJpaEntity storeJpaEntity) {
        ProductBrandStoreJpaEntity entity = new ProductBrandStoreJpaEntity();
        entity.setId(productBrandStore.getId());
        entity.setProductBrand(productBrandJpaEntity);
        entity.setStore(storeJpaEntity);
        entity.setSearchName(productBrandStore.getSearchName());
        entity.setActive(productBrandStore.isActive());
        return entity;
    }

    public ProductBrandStore toDomain(ProductBrandStoreJpaEntity entity) {
        return ProductBrandStore.restore(
            entity.getId(),
            entity.getProductBrand().getId(),
            entity.getStore().getId(),
            entity.getSearchName(),
            entity.isActive()
        );
    }
}
