package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Role;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface RoleRepository {

    Role save(Role role);
    Optional<Role> findById(UUID id);
    DomainPageResult<Role> findAll(DomainPageRequest pageRequest);
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
