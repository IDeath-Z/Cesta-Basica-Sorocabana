package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.result;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultJpaRepository extends JpaRepository<ResultJpaEntity, UUID> {
}
