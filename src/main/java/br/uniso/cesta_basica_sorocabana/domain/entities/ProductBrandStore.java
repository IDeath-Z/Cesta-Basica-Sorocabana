package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.ProductBrandStoreError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class ProductBrandStore {

    private final UUID id;
    private final UUID productBrandId;
    private final UUID storeId;
    private final String searchName;
    private final boolean active;

    private ProductBrandStore(UUID id, UUID productBrandId, UUID storeId, String searchName, boolean active) {
        this.id = DomainValidator.requireNonNull(id, ProductBrandStoreError.ID_REQUIRED.message());
        this.productBrandId = DomainValidator.requireNonNull(productBrandId, ProductBrandStoreError.PRODUCT_BRAND_ID_REQUIRED.message());
        this.storeId = DomainValidator.requireNonNull(storeId, ProductBrandStoreError.STORE_ID_REQUIRED.message());
        this.searchName = normalizeSearchName(searchName);
        this.active = active;
    }

    // Null means the store does not carry the item. A blank text is a mistake, not a null.
    private static String normalizeSearchName(String searchName) {
        if (searchName == null)
            return null;

        return DomainValidator.requireNonBlank(searchName, ProductBrandStoreError.SEARCH_NAME_BLANK.message()).strip();
    }

    public static ProductBrandStore createNew(UUID productBrandId, UUID storeId, String searchName) {
        return new ProductBrandStore(UUID.randomUUID(), productBrandId, storeId, searchName, true);
    }

    public static ProductBrandStore restore(UUID id, UUID productBrandId, UUID storeId, String searchName, boolean active) {
        return new ProductBrandStore(id, productBrandId, storeId, searchName, active);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductBrandId() {
        return productBrandId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public String getSearchName() {
        return searchName;
    }

    public boolean isActive() {
        return active;
    }

    public ProductBrandStore updateSearchName(String searchName) {
        return new ProductBrandStore(this.id, this.productBrandId, this.storeId, searchName, this.active);
    }

    public ProductBrandStore activate() {
        if (this.active)
            return this;

        return new ProductBrandStore(this.id, this.productBrandId, this.storeId, this.searchName, true);
    }

    public ProductBrandStore deactivate() {
        if (!this.active)
            return this;

        return new ProductBrandStore(this.id, this.productBrandId, this.storeId, this.searchName, false);
    }
}
