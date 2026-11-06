package br.uniso.cesta_basica_sorocabana.application.usecases.basket;

import java.math.BigDecimal;
import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.shared.BasketItem;

public record BasketItemOutput(
    UUID id,
    UUID productBrandId,
    String searchName,
    UUID productId,
    String productName,
    String productSpecification,
    String productNotes,
    UUID categoryId,
    String categoryName,
    UUID unitId,
    BigDecimal unitQuantity,
    String unitMeasure,
    UUID brandId,
    String brandName,
    UUID storeId,
    String storeName,
    String storeUrl,
    BigDecimal price
) {
    public static BasketItemOutput from(BasketItem item) {
        return new BasketItemOutput(item.id(), item.productBrandId(), item.searchName(), item.productId(), item.productName(),
            item.productSpecification(), item.productNotes(), item.categoryId(), item.categoryName(), item.unitId(),
            item.unitQuantity(), item.unitMeasure(), item.brandId(), item.brandName(), item.storeId(), item.storeName(),
            item.storeUrl(), item.price());
    }
}
