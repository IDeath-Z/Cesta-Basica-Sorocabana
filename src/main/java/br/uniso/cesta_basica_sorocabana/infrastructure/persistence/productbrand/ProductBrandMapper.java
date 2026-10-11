package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.productbrand;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.ProductBrand;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.brand.BrandJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.product.ProductJpaEntity;

@Component
public final class ProductBrandMapper {

    public ProductBrandJpaEntity toJpa(ProductBrand productBrand, ProductJpaEntity productJpaEntity, BrandJpaEntity brandJpaEntity) {
        ProductBrandJpaEntity entity = new ProductBrandJpaEntity();
        entity.setId(productBrand.getId());
        entity.setProduct(productJpaEntity);
        entity.setBrand(brandJpaEntity);
        entity.setActive(productBrand.isActive());
        return entity;
    }

    public ProductBrand toDomain(ProductBrandJpaEntity entity) {
        return ProductBrand.restore(
            entity.getId(),
            entity.getProduct().getId(),
            entity.getBrand().getId(),
            entity.isActive()
        );
    }
}
