package br.uniso.cesta_basica_sorocabana.presentation.dto.basket;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.application.usecases.basket.BasketItemOutput;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BasketItemResponse", description = "A basket item in one store, with everything about it")
public record BasketItemResponse(
    @Schema(description = "ID of the item + store link", example = "b7a6c5d4-e3f2-4a1b-9c8d-7e6f5a4b3c2d")
    UUID id,

    @Schema(description = "Product brand ID. This is the id to send when saving prices", example = "e4d3c2b1-a0f9-4e8d-7c6b-5a4f3e2d1c0b")
    UUID productBrandId,

    @Schema(description = "How this store names the item, brand included", example = "Arroz Camil tipo 1 5kg")
    String searchName,

    @Schema(description = "Product ID", example = "9d8c7b6a-5f4e-4d3c-2b1a-0f9e8d7c6b5a")
    UUID productId,

    @Schema(description = "Product name", example = "Arroz")
    String productName,

    @Schema(description = "Product specification", example = "Tipo 1")
    String productSpecification,

    @Schema(description = "Product notes", example = "Long grain white rice")
    String productNotes,

    @Schema(description = "Category ID", example = "5b2e7c1d-9f4a-4d3b-8e6c-2a1f0b9c8d7e")
    UUID categoryId,

    @Schema(description = "Category name", example = "Alimentação")
    String categoryName,

    @Schema(description = "Unit ID. Null only on products registered before the unit became mandatory", example = "7a9d4e21-3c5b-4f8a-b6d2-9e1f0a3b5c7d")
    UUID unitId,

    @Schema(description = "Unit quantity", example = "5")
    BigDecimal unitQuantity,

    @Schema(description = "Unit measure", example = "kg")
    String unitMeasure,

    @Schema(description = "Brand ID", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f")
    UUID brandId,

    @Schema(description = "Brand name", example = "Camil")
    String brandName,

    @Schema(description = "Store ID", example = "3f1c2a9e-8b7d-4c6a-9e2f-1a2b3c4d5e6f")
    UUID storeId,

    @Schema(description = "Store name", example = "Tauste")
    String storeName,

    @Schema(description = "Store website", example = "https://tauste.com.br/sorocaba1")
    String storeUrl,

    @Schema(description = "Price saved in the survey. Always null in the plain basket: a price exists only inside a survey", example = "25.90")
    BigDecimal price
) {
    public static BasketItemResponse from(BasketItemOutput output) {
        return new BasketItemResponse(output.id(), output.productBrandId(), output.searchName(), output.productId(),
            output.productName(), output.productSpecification(), output.productNotes(), output.categoryId(), output.categoryName(),
            output.unitId(), output.unitQuantity(), output.unitMeasure(), output.brandId(), output.brandName(), output.storeId(),
            output.storeName(), output.storeUrl(), output.price());
    }
}
