package br.uniso.cesta_basica_sorocabana.infrastructure.persistence.category;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.uniso.cesta_basica_sorocabana.domain.entities.Category;
import br.uniso.cesta_basica_sorocabana.domain.ports.CategoryRepository;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageResult;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public final class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = categoryMapper.toJpa(category);
        return categoryMapper.toDomain(categoryJpaRepository.save(entity));
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaRepository.findById(id).map(categoryMapper::toDomain);
    }

    @Override
    public DomainPageResult<Category> findAll(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<CategoryJpaEntity> page = categoryJpaRepository.findAll(springPage);

        return PageConverter.toDomain(page, categoryMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }
}
