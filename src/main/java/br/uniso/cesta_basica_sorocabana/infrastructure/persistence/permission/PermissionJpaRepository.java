package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, String> {
}
