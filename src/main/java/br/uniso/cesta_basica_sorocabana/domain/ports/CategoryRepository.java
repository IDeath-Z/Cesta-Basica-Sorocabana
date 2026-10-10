package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Category;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface CategoryRepository {

    Category save(Category category);
    Optional<Category> findById(UUID id);
    DomainPageResult<Category> findAll(DomainPageRequest pageRequest);
    boolean existsByName(String name);
}
