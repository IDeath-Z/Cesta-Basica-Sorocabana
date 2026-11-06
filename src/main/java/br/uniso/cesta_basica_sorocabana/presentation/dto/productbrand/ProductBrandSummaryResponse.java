package br.uniso.cesta_basica_sorocabana.presentation.dto.productbrand;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.ProductBrandSummaryOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductBrandSummaryResponse", description = "A product + brand pair with the names needed to tell it apart")
public record ProductBrandSummaryResponse(
    @Schema(description = "Unique product brand identifier", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b")
    UUID id,

    @Schema(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a")
    UUID productId,

    @Schema(description = "Product name", example = "Arroz")
    String productName,

    @Schema(description = "Product specification", example = "tipo 1")
    String productSpecification,

    @Schema(description = "Unit quantity. Null only on products registered before the unit became mandatory", example = "5")
    BigDecimal unitQuantity,

    @Schema(description = "Unit measure", example = "kg")
    String unitMeasure,

    @Schema(description = "Brand ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID brandId,

    @Schema(description = "Brand name", example = "Camil")
    String brandName,

    @Schema(description = "Whether the pair can be linked to stores and surveyed", example = "true")
    boolean active
) {
    public static ProductBrandSummaryResponse from(ProductBrandSummaryOutput output) {
        return new ProductBrandSummaryResponse(output.id(), output.productId(), output.productName(), output.productSpecification(),
            output.unitQuantity(), output.unitMeasure(), output.brandId(), output.brandName(), output.active());
    }
}
