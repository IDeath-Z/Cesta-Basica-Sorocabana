package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.role;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, UUID> {

    // Permissions are fetched together because the security filter reads them outside a transaction.
    @EntityGraph(attributePaths = "permissions")
    Optional<RoleJpaEntity> findWithPermissionsById(UUID id);

    @EntityGraph(attributePaths = "permissions")
    Optional<RoleJpaEntity> findByName(String name);

    boolean existsByName(String name);
}
