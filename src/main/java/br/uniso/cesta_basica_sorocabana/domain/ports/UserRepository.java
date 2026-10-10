package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.User;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    DomainPageResult<User> findAll(DomainPageRequest pageRequest);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
