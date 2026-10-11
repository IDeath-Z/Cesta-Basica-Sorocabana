package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.product;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    Page<ProductJpaEntity> findByCategoryId(UUID categoryId, Pageable pageable);
    boolean existsByNameAndSpecification(String name, String specification);
    boolean existsByNameAndSpecificationIsNull(String name);
}
