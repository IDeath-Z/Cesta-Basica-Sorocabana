package br.uniso.cesta_basica_sorocabana.presentation.dto.productbrand;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.productbrand.ProductBrandOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductBrandResponse", description = "A product + brand pair that can be priced in surveys, e.g. Arroz Camil")
public record ProductBrandResponse(
    @Schema(description = "Unique product brand identifier", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b")
    UUID id,

    @Schema(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a")
    UUID productId,

    @Schema(description = "Brand ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID brandId,

    @Schema(description = "Whether the pair can be added to new surveys", example = "true")
    boolean active
) {
    public static ProductBrandResponse from(ProductBrandOutput output) {
        return new ProductBrandResponse(output.id(), output.productId(), output.brandId(), output.active());
    }
}
