package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.store;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<StoreJpaEntity, UUID> {

    boolean existsByName(String name);
}
