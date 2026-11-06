package br.uniso.cesta_basica_sorocabana.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.domain.shared.ProductBrandSummary;

public interface ProductBrandRepository {

    ProductBrand save(ProductBrand productBrand);
    Optional<ProductBrand> findById(UUID id);
    List<ProductBrand> findByProductId(UUID productId);
    DomainPageResult<ProductBrandSummary> findAllSummaries(int page, int size);
    boolean existsByProductIdAndBrandId(UUID productId, UUID brandId);
}
