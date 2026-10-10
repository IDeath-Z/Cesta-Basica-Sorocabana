package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;

public interface ProductBrandRepository {

    ProductBrand save(ProductBrand productBrand);
    Optional<ProductBrand> findById(UUID id);
    List<ProductBrand> findByProductId(UUID productId);
    boolean existsByProductIdAndBrandId(UUID productId, UUID brandId);
}
