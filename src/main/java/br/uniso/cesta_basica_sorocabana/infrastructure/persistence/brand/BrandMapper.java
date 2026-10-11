package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.brand;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Brand;

@Component
public final class BrandMapper {

    public BrandJpaEntity toJpa(Brand brand) {
        BrandJpaEntity entity = new BrandJpaEntity();
        entity.setId(brand.getId());
        entity.setName(brand.getName());
        entity.setActive(brand.isActive());
        return entity;
    }

    public Brand toDomain(BrandJpaEntity entity) {
        return Brand.restore(
            entity.getId(),
            entity.getName(),
            entity.isActive()
        );
    }
}
