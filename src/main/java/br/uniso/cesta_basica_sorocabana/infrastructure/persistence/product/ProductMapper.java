package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.product;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Product;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.category.CategoryJpaEntity;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.unit.UnitJpaEntity;

@Component
public final class ProductMapper {

    public ProductJpaEntity toJpa(Product product, CategoryJpaEntity categoryJpaEntity, UnitJpaEntity unitJpaEntity) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(product.getId());
        entity.setCategory(categoryJpaEntity);
        entity.setUnit(unitJpaEntity);
        entity.setName(product.getName());
        entity.setSpecification(product.getSpecification());
        entity.setNotes(product.getNotes());
        entity.setActive(product.isActive());
        return entity;
    }

    public Product toDomain(ProductJpaEntity entity) {
        return Product.restore(
            entity.getId(),
            entity.getCategory().getId(),
            entity.getUnit() != null ? entity.getUnit().getId() : null,
            entity.getName(),
            entity.getSpecification(),
            entity.getNotes(),
            entity.isActive()
        );
    }
}
