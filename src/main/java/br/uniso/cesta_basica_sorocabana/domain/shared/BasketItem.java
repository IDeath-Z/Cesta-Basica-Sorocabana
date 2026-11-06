package br.uniso.cesta_basica_sorocabana.domain.shared;

import java.math.BigDecimal;
import java.util.UUID;

// Read model of a basket item in one store. The price exists only inside a survey, so it is null in the plain basket.
public record BasketItem(
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
    public BasketItem(UUID id, UUID productBrandId, String searchName, UUID productId, String productName,
        String productSpecification, String productNotes, UUID categoryId, String categoryName, UUID unitId,
        BigDecimal unitQuantity, String unitMeasure, UUID brandId, String brandName, UUID storeId, String storeName,
        String storeUrl) {
        this(id, productBrandId, searchName, productId, productName, productSpecification, productNotes, categoryId,
            categoryName, unitId, unitQuantity, unitMeasure, brandId, brandName, storeId, storeName, storeUrl, null);
    }
}
