package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.category;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, UUID> {

    boolean existsByName(String name);
}
