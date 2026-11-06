package br.uniso.cesta_basica_sorocabana.application.usecases.productbrand;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.shared.ProductBrandSummary;

public record ProductBrandSummaryOutput(UUID id, UUID productId, String productName, String productSpecification,
    BigDecimal unitQuantity, String unitMeasure, UUID brandId, String brandName, boolean active) {

    public static ProductBrandSummaryOutput from(ProductBrandSummary summary) {
        return new ProductBrandSummaryOutput(summary.id(), summary.productId(), summary.productName(), summary.productSpecification(),
            summary.unitQuantity(), summary.unitMeasure(), summary.brandId(), summary.brandName(), summary.active());
    }
}
