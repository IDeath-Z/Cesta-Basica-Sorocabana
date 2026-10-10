package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.Product;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;

public interface ProductRepository {

    Product save(Product product);
    Optional<Product> findById(UUID id);
    DomainPageResult<Product> findAll(DomainPageRequest pageRequest);
    DomainPageResult<Product> findByCategoryId(UUID categoryId, DomainPageRequest pageRequest);
    boolean existsByNameAndSpecification(String name, String specification);
}
