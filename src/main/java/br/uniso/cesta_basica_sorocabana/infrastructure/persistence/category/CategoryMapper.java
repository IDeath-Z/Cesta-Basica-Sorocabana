package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.category;

import org.springframework.stereotype.Component;

import br.uniso.cesta_basica_sorocabana.domain.entities.Category;

@Component
public final class CategoryMapper {

    public CategoryJpaEntity toJpa(Category category) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setActive(category.isActive());
        return entity;
    }

    public Category toDomain(CategoryJpaEntity entity) {
        return Category.restore(
            entity.getId(),
            entity.getName(),
            entity.isActive()
        );
    }
}
