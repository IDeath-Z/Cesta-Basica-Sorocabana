package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface BrandRepository {

    Brand save(Brand brand);
    Optional<Brand> findById(UUID id);
    DomainPageResult<Brand> findAll(DomainPageRequest pageRequest);
    boolean existsByName(String name);
}
