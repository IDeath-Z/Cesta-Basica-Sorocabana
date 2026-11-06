package br.uniso.cesta_basica_sorocabana.domain.shared;

import java.math.BigDecimal;
import java.util.UUID;

// Read model of a product + brand pair with the names needed to tell it apart.
public record ProductBrandSummary(
    UUID id,
    UUID productId,
    String productName,
    String productSpecification,
    BigDecimal unitQuantity,
    String unitMeasure,
    UUID brandId,
    String brandName,
    boolean active
) {}
