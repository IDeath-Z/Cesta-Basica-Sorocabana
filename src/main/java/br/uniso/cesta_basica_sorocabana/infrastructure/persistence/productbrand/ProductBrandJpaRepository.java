package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBrandJpaRepository extends JpaRepository<ProductBrandJpaEntity, UUID> {

    List<ProductBrandJpaEntity> findByProductId(UUID productId);
    boolean existsByProductIdAndBrandId(UUID productId, UUID brandId);
}
