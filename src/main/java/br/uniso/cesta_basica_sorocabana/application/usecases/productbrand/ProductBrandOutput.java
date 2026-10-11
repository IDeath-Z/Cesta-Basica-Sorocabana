package br.uniso.cesta_basica_sorocabana.application.usecases.productbrand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;

public record ProductBrandOutput(UUID id, UUID productId, UUID brandId, boolean active) {

    public static ProductBrandOutput from(ProductBrand productBrand) {
        return new ProductBrandOutput(productBrand.getId(), productBrand.getProductId(), productBrand.getBrandId(),
            productBrand.isActive());
    }
}
