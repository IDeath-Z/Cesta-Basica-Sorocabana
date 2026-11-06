package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrandstore;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBrandStoreJpaRepository extends JpaRepository<ProductBrandStoreJpaEntity, UUID> {

    List<ProductBrandStoreJpaEntity> findByProductBrandId(UUID productBrandId);
    boolean existsByProductBrandIdAndStoreId(UUID productBrandId, UUID storeId);
}
