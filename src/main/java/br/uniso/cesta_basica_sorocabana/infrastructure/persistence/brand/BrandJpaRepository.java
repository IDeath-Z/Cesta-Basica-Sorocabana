package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.brand;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, UUID> {

    boolean existsByName(String name);
}
