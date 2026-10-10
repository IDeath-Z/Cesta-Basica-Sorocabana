package br.uniso.cesta_basica_sorocabana.domain.entities;

import java.util.UUID;

import br.uniso.cesta_basica_sorocabana.domain.enums.errors.ProductBrandError;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainValidator;

public final class ProductBrand {

    private final UUID id;
    private final UUID productId;
    private final UUID brandId;
    private final boolean active;

    private ProductBrand(UUID id, UUID productId, UUID brandId, boolean active) {
        this.id = DomainValidator.requireNonNull(id, ProductBrandError.ID_REQUIRED.message());
        this.productId = DomainValidator.requireNonNull(productId, ProductBrandError.PRODUCT_ID_REQUIRED.message());
        this.brandId = DomainValidator.requireNonNull(brandId, ProductBrandError.BRAND_ID_REQUIRED.message());
        this.active = active;
    }

    public static ProductBrand createNew(UUID productId, UUID brandId) {
        return new ProductBrand(UUID.randomUUID(), productId, brandId, true);
    }

    public static ProductBrand restore(UUID id, UUID productId, UUID brandId, boolean active) {
        return new ProductBrand(id, productId, brandId, active);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getBrandId() {
        return brandId;
    }

    public boolean isActive() {
        return active;
    }

    public ProductBrand activate() {
        if (this.active)
            return this;

        return new ProductBrand(this.id, this.productId, this.brandId, true);
    }

    public ProductBrand deactivate() {
        if (!this.active)
            return this;

        return new ProductBrand(this.id, this.productId, this.brandId, false);
    }
}
